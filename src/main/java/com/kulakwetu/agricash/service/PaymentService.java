package com.kulakwetu.agricash.service;

import com.kulakwetu.agricash.dto.*;
import com.kulakwetu.agricash.entity.*;
import com.kulakwetu.agricash.enums.LedgerAccountType;
import com.kulakwetu.agricash.enums.PaymentIntentStatus;
import com.kulakwetu.agricash.enums.PaymentOperationType;
import com.kulakwetu.agricash.repository.*;
import com.kulakwetu.agrisol.entity.Order;
import com.kulakwetu.agrisol.repository.OrderRepository;
import com.kulakwetu.common.exception.DomainException;
import com.kulakwetu.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentIntentRepository paymentIntentRepository;
    private final PaymentOperationRepository paymentOperationRepository;
    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;
    private final WalletBalanceRepository walletBalanceRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final LedgerService ledgerService;
    private final LedgerEntryRepository ledgerEntryRepository;

    @Transactional
    public PaymentIntentResponse createIntent(CreatePaymentIntentRequest request) {
        return paymentOperationRepository.findByOperationTypeAndIdempotencyKey(PaymentOperationType.CREATE_INTENT, request.idempotencyKey())
                .map(op -> toResponse(op.getPaymentIntent()))
                .orElseGet(() -> doCreateIntent(request));
    }

    @Transactional
    public PaymentIntentResponse capture(UUID id, CapturePaymentRequest request) {
        return paymentOperationRepository.findByOperationTypeAndIdempotencyKey(PaymentOperationType.CAPTURE, request.idempotencyKey())
                .map(op -> toResponse(op.getPaymentIntent()))
                .orElseGet(() -> doCapture(id, request));
    }

    @Transactional
    public PaymentIntentResponse refund(UUID id, RefundPaymentRequest request) {
        return paymentOperationRepository.findByOperationTypeAndIdempotencyKey(PaymentOperationType.REFUND, request.idempotencyKey())
                .map(op -> toResponse(op.getPaymentIntent()))
                .orElseGet(() -> doRefund(id, request));
    }

    @Transactional(readOnly = true)
    public PaymentIntentResponse get(UUID id) {
        return toResponse(paymentIntentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment intent not found")));
    }

    private PaymentIntentResponse doCreateIntent(CreatePaymentIntentRequest request) {
        if (paymentIntentRepository.existsByOrderId(request.orderId())) {
            return toResponse(paymentIntentRepository.findByOrderId(request.orderId())
                    .orElseThrow(() -> new DomainException("Payment intent already exists for order")));
        }

        Order order = orderRepository.findById(request.orderId()).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUserId().equals(currentUserId())) throw new DomainException("Cannot pay another user's order");

        PaymentIntent intent = paymentIntentRepository.save(PaymentIntent.builder()
                .id(UUID.randomUUID())
                .orderId(order.getId())
                .payerUserId(order.getUserId())
                .amount(order.getTotal())
                .currencyCode(order.getCurrencyCode())
                .status(PaymentIntentStatus.CREATED)
                .capturedAmount(BigDecimal.ZERO)
                .refundedAmount(BigDecimal.ZERO)
                .idempotencyKey(request.idempotencyKey())
                .createdAt(OffsetDateTime.now())
                .build());

        saveOperation(intent, PaymentOperationType.CREATE_INTENT, request.idempotencyKey());
        return toResponse(intent);
    }

    private PaymentIntentResponse doCapture(UUID id, CapturePaymentRequest request) {
        PaymentIntent intent = paymentIntentRepository.lockById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment intent not found"));

        if (intent.getStatus() == PaymentIntentStatus.CAPTURED || intent.getStatus() == PaymentIntentStatus.REFUNDED) {
            throw new DomainException("Already captured");
        }

        UUID payerUserId = intent.getPayerUserId();
        String currencyCode = intent.getCurrencyCode();

        Wallet wallet = walletRepository.findByUserId(payerUserId).orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        WalletBalance balance = walletBalanceRepository.findByWalletIdAndCurrencyCodeForUpdate(wallet.getId(), currencyCode)
                .orElseThrow(() -> new DomainException("Wallet balance not found for currency"));

        if (balance.getAvailableAmount().compareTo(intent.getAmount()) < 0) throw new DomainException("Insufficient funds");

        // available -> reserved
        balance.setAvailableAmount(balance.getAvailableAmount().subtract(intent.getAmount()));
        balance.setReservedAmount(balance.getReservedAmount().add(intent.getAmount()));
        walletBalanceRepository.save(balance);

        intent.setStatus(PaymentIntentStatus.PENDING);
        paymentIntentRepository.save(intent);

        LedgerAccount payerWalletAccount = ledgerAccountRepository.findByTypeAndOwnerIdAndCurrencyCode(
                        LedgerAccountType.WALLET, payerUserId, currencyCode)
                .orElseGet(() -> ledgerAccountRepository.save(LedgerAccount.builder()
                        .id(UUID.randomUUID())
                        .type(LedgerAccountType.WALLET)
                        .ownerId(payerUserId)
                        .currencyCode(currencyCode)
                        .build()));

        LedgerAccount clearing = ledgerAccountRepository.findByTypeAndCurrencyCodeAndOwnerIdIsNull(
                        LedgerAccountType.CLEARING, currencyCode)
                .orElseGet(() -> ledgerAccountRepository.save(LedgerAccount.builder()
                        .id(UUID.randomUUID())
                        .type(LedgerAccountType.CLEARING)
                        .ownerId(null)
                        .currencyCode(currencyCode)
                        .build()));

        LedgerJournalResponse journal;
        try {
            journal = ledgerService.postJournal(new PostJournalRequest(
                    "PAYMENT_CAPTURE",
                    intent.getId(),
                    List.of(
                            new LedgerEntryRequest(payerWalletAccount.getId(), intent.getAmount(), BigDecimal.ZERO, currencyCode),
                            new LedgerEntryRequest(clearing.getId(), BigDecimal.ZERO, intent.getAmount(), currencyCode)
                    )
            ));
            intent.setStatus(PaymentIntentStatus.SUCCEEDED);
        } catch (RuntimeException ex) {
            intent.setStatus(PaymentIntentStatus.FAILED);
            paymentIntentRepository.save(intent);
            throw ex;
        }

        WalletBalance after = walletBalanceRepository.findByWalletIdAndCurrencyCodeForUpdate(wallet.getId(), currencyCode)
                .orElseThrow(() -> new DomainException("Wallet balance not found for settle"));
        after.setReservedAmount(after.getReservedAmount().subtract(intent.getAmount()).max(BigDecimal.ZERO));
        walletBalanceRepository.save(after);

        intent.setStatus(PaymentIntentStatus.CAPTURED);
        intent.setCapturedAmount(intent.getAmount());
        intent.setProviderRef(journal.journalId().toString());
        paymentIntentRepository.save(intent);
        saveOperation(intent, PaymentOperationType.CAPTURE, request.idempotencyKey());
        return toResponse(intent);
    }

    private PaymentIntentResponse doRefund(UUID id, RefundPaymentRequest request) {
        PaymentIntent intent = paymentIntentRepository.lockById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment intent not found"));

        if (intent.getStatus() != PaymentIntentStatus.CAPTURED && intent.getStatus() != PaymentIntentStatus.REFUNDED) {
            throw new DomainException("Only captured payments can be refunded");
        }

        BigDecimal remaining = intent.getCapturedAmount().subtract(intent.getRefundedAmount());
        if (request.amount().compareTo(remaining) > 0) throw new DomainException("Refund amount exceeds captured amount");

        if (intent.getProviderRef() == null || intent.getProviderRef().isBlank()) {
            throw new DomainException("Missing capture journal reference");
        }

        UUID captureJournalId = UUID.fromString(intent.getProviderRef());
        List<LedgerEntry> originalEntries = ledgerEntryRepository.findByJournalId(captureJournalId);
        BigDecimal ratio = request.amount().divide(intent.getCapturedAmount(), 8, RoundingMode.HALF_UP);

        List<LedgerEntryRequest> reversals = originalEntries.stream().map(e -> {
            BigDecimal debit = e.getCredit().multiply(ratio).setScale(4, RoundingMode.HALF_UP);
            BigDecimal credit = e.getDebit().multiply(ratio).setScale(4, RoundingMode.HALF_UP);
            return new LedgerEntryRequest(e.getAccount().getId(), debit, credit, e.getCurrencyCode());
        }).toList();

        ledgerService.postJournal(new PostJournalRequest("PAYMENT_REFUND", intent.getId(), reversals));

        intent.setRefundedAmount(intent.getRefundedAmount().add(request.amount()));
        intent.setStatus(intent.getRefundedAmount().compareTo(intent.getCapturedAmount()) >= 0
                ? PaymentIntentStatus.REFUNDED : PaymentIntentStatus.CAPTURED);
        paymentIntentRepository.save(intent);
        saveOperation(intent, PaymentOperationType.REFUND, request.idempotencyKey());
        return toResponse(intent);
    }

    private void saveOperation(PaymentIntent intent, PaymentOperationType type, String key) {
        paymentOperationRepository.save(PaymentOperation.builder()
                .id(UUID.randomUUID())
                .paymentIntent(intent)
                .operationType(type)
                .idempotencyKey(key)
                .createdAt(OffsetDateTime.now())
                .build());
    }

    private PaymentIntentResponse toResponse(PaymentIntent p) {
        return new PaymentIntentResponse(p.getId(), p.getOrderId(), p.getPayerUserId(), p.getAmount(), p.getCurrencyCode(),
                p.getStatus(), p.getCapturedAmount(), p.getRefundedAmount(), p.getIdempotencyKey(), p.getProviderRef());
    }

    private UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(auth.getName());
    }
}
