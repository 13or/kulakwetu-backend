package com.kulakwetu.agricash.service;

import com.kulakwetu.agricash.dto.LedgerEntryRequest;
import com.kulakwetu.agricash.dto.PostJournalRequest;
import com.kulakwetu.agricash.dto.WithdrawalCreateRequest;
import com.kulakwetu.agricash.dto.WithdrawalResponse;
import com.kulakwetu.agricash.entity.LedgerAccount;
import com.kulakwetu.agricash.entity.WithdrawalRequest;
import com.kulakwetu.agricash.enums.LedgerAccountType;
import com.kulakwetu.agricash.enums.WithdrawalStatus;
import com.kulakwetu.agricash.gateway.MobileMoneyGateway;
import com.kulakwetu.agricash.repository.LedgerAccountRepository;
import com.kulakwetu.agricash.repository.LedgerEntryRepository;
import com.kulakwetu.agricash.repository.WithdrawalRequestRepository;
import com.kulakwetu.common.exception.DomainException;
import com.kulakwetu.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WithdrawalService {

    private static final BigDecimal DAILY_LIMIT = new BigDecimal("5000.0000");
    private static final long COOLDOWN_MINUTES = 5;

    private final WithdrawalRequestRepository withdrawalRequestRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerService ledgerService;
    private final MobileMoneyGateway mobileMoneyGateway;

    @Transactional
    public WithdrawalResponse request(WithdrawalCreateRequest req) {
        return withdrawalRequestRepository.findByIdempotencyKey(req.idempotencyKey()).map(this::toResponse).orElseGet(() -> doRequest(req));
    }

    @Transactional(readOnly = true)
    public List<WithdrawalResponse> myRequests() {
        return withdrawalRequestRepository.findBySupplierUserIdOrderByCreatedAtDesc(currentUserId()).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<WithdrawalResponse> pending() {
        return withdrawalRequestRepository.findByStatusOrderByCreatedAtDesc(WithdrawalStatus.REQUESTED).stream().map(this::toResponse).toList();
    }

    @Transactional
    public WithdrawalResponse approve(UUID id, String idempotencyKey) {
        return withdrawalRequestRepository.findByIdempotencyKey(idempotencyKey)
                .filter(w -> w.getStatus() != WithdrawalStatus.REQUESTED)
                .map(this::toResponse)
                .orElseGet(() -> doApprove(id, idempotencyKey));
    }

    @Transactional
    public WithdrawalResponse reject(UUID id, String reason, String idempotencyKey) {
        return withdrawalRequestRepository.findByIdempotencyKey(idempotencyKey)
                .filter(w -> w.getStatus() == WithdrawalStatus.REJECTED)
                .map(this::toResponse)
                .orElseGet(() -> doReject(id, reason, idempotencyKey));
    }

    private WithdrawalResponse doRequest(WithdrawalCreateRequest req) {
        UUID supplierId = currentUserId();

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime startDay = now.toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC);
        BigDecimal sumToday = withdrawalRequestRepository.sumToday(supplierId,
                List.of(WithdrawalStatus.REQUESTED, WithdrawalStatus.APPROVED, WithdrawalStatus.PAYOUT_PENDING, WithdrawalStatus.PAID),
                startDay, now);
        if (sumToday.add(req.amount()).compareTo(DAILY_LIMIT) > 0) {
            throw new DomainException("Daily withdrawal limit exceeded");
        }

        OffsetDateTime last = withdrawalRequestRepository.lastRequestAt(supplierId);
        if (last != null && last.isAfter(now.minusMinutes(COOLDOWN_MINUTES))) {
            throw new DomainException("Withdrawal cooldown active");
        }

        LedgerAccount supplierPayable = payableAccount(supplierId, req.currencyCode());
        BigDecimal payableBalance = ledgerEntryRepository.findByAccountOwnerIdOrderByJournalCreatedAtDesc(supplierId).stream()
                .filter(e -> e.getAccount().getId().equals(supplierPayable.getId()))
                .map(e -> e.getCredit().subtract(e.getDebit()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (payableBalance.compareTo(req.amount()) < 0) throw new DomainException("Supplier payable balance is insufficient");

        WithdrawalRequest wr = withdrawalRequestRepository.save(WithdrawalRequest.builder()
                .id(UUID.randomUUID())
                .supplierUserId(supplierId)
                .amount(req.amount())
                .currencyCode(req.currencyCode())
                .status(WithdrawalStatus.REQUESTED)
                .idempotencyKey(req.idempotencyKey())
                .payoutMethod(req.payoutMethod())
                .payoutAccount(req.payoutAccount())
                .createdAt(now)
                .build());

        return toResponse(wr);
    }

    private WithdrawalResponse doApprove(UUID id, String key) {
        WithdrawalRequest wr = withdrawalRequestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Withdrawal request not found"));
        if (wr.getStatus() != WithdrawalStatus.REQUESTED) throw new DomainException("Only requested withdrawal can be approved");

        LedgerAccount supplierPayable = payableAccount(wr.getSupplierUserId(), wr.getCurrencyCode());
        LedgerAccount platformClearing = platformClearing(wr.getCurrencyCode());

        ledgerService.postJournal(new PostJournalRequest(
                "WITHDRAWAL_APPROVE", wr.getId(),
                List.of(
                        new LedgerEntryRequest(supplierPayable.getId(), wr.getAmount(), BigDecimal.ZERO, wr.getCurrencyCode()),
                        new LedgerEntryRequest(platformClearing.getId(), BigDecimal.ZERO, wr.getAmount(), wr.getCurrencyCode())
                )
        ));

        wr.setStatus(WithdrawalStatus.APPROVED);
        wr.setIdempotencyKey(key);

        wr.setStatus(WithdrawalStatus.PAYOUT_PENDING);
        try {
            String externalRef = mobileMoneyGateway.payout(wr.getSupplierUserId(), wr.getAmount(), wr.getCurrencyCode(), wr.getPayoutMethod(), wr.getPayoutAccount());
            wr.setExternalReference(externalRef);

            LedgerAccount bankOutflow = bankOutflow(wr.getCurrencyCode());
            ledgerService.postJournal(new PostJournalRequest(
                    "WITHDRAWAL_PAID", wr.getId(),
                    List.of(
                            new LedgerEntryRequest(platformClearing.getId(), wr.getAmount(), BigDecimal.ZERO, wr.getCurrencyCode()),
                            new LedgerEntryRequest(bankOutflow.getId(), BigDecimal.ZERO, wr.getAmount(), wr.getCurrencyCode())
                    )
            ));
            wr.setStatus(WithdrawalStatus.PAID);
        } catch (RuntimeException ex) {
            wr.setStatus(WithdrawalStatus.FAILED);
            throw ex;
        } finally {
            withdrawalRequestRepository.save(wr);
        }

        return toResponse(wr);
    }

    private WithdrawalResponse doReject(UUID id, String reason, String key) {
        WithdrawalRequest wr = withdrawalRequestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Withdrawal request not found"));
        if (wr.getStatus() != WithdrawalStatus.REQUESTED) throw new DomainException("Only requested withdrawal can be rejected");
        wr.setStatus(WithdrawalStatus.REJECTED);
        wr.setRejectionReason(reason);
        wr.setIdempotencyKey(key);
        return toResponse(withdrawalRequestRepository.save(wr));
    }

    private LedgerAccount payableAccount(UUID supplierId, String currency) {
        return ledgerAccountRepository.findByTypeAndOwnerIdAndCurrencyCode(LedgerAccountType.SUPPLIER_PAYABLE, supplierId, currency)
                .orElseGet(() -> ledgerAccountRepository.save(LedgerAccount.builder()
                        .id(UUID.randomUUID()).type(LedgerAccountType.SUPPLIER_PAYABLE).ownerId(supplierId).currencyCode(currency).build()));
    }

    private LedgerAccount platformClearing(String currency) {
        return ledgerAccountRepository.findByTypeAndCurrencyCodeAndOwnerIdIsNull(LedgerAccountType.PLATFORM_CLEARING, currency)
                .orElseGet(() -> ledgerAccountRepository.save(LedgerAccount.builder()
                        .id(UUID.randomUUID()).type(LedgerAccountType.PLATFORM_CLEARING).currencyCode(currency).build()));
    }

    private LedgerAccount bankOutflow(String currency) {
        return ledgerAccountRepository.findByTypeAndCurrencyCodeAndOwnerIdIsNull(LedgerAccountType.BANK_OUTFLOW, currency)
                .orElseGet(() -> ledgerAccountRepository.save(LedgerAccount.builder()
                        .id(UUID.randomUUID()).type(LedgerAccountType.BANK_OUTFLOW).currencyCode(currency).build()));
    }

    private WithdrawalResponse toResponse(WithdrawalRequest w) {
        return new WithdrawalResponse(w.getId(), w.getSupplierUserId(), w.getAmount(), w.getCurrencyCode(), w.getStatus(),
                w.getPayoutMethod(), w.getPayoutAccount(), w.getRejectionReason(), w.getExternalReference());
    }

    private UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(auth.getName());
    }
}
