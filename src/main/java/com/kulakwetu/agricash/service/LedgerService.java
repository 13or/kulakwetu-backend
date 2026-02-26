package com.kulakwetu.agricash.service;

import com.kulakwetu.agricash.dto.LedgerEntryRequest;
import com.kulakwetu.agricash.dto.LedgerJournalResponse;
import com.kulakwetu.agricash.dto.PostJournalRequest;
import com.kulakwetu.agricash.entity.*;
import com.kulakwetu.agricash.enums.LedgerAccountType;
import com.kulakwetu.agricash.enums.LedgerJournalStatus;
import com.kulakwetu.agricash.repository.*;
import com.kulakwetu.common.exception.DomainException;
import com.kulakwetu.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerJournalRepository ledgerJournalRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final WalletRepository walletRepository;
    private final WalletBalanceRepository walletBalanceRepository;

    @Transactional
    public LedgerJournalResponse postJournal(PostJournalRequest request) {
        validateDoubleEntry(request.entries());

        LedgerJournal journal = ledgerJournalRepository.save(LedgerJournal.builder()
                .id(UUID.randomUUID())
                .referenceType(request.referenceType())
                .referenceId(request.referenceId())
                .status(LedgerJournalStatus.POSTED)
                .createdAt(OffsetDateTime.now())
                .build());

        for (LedgerEntryRequest e : request.entries()) {
            LedgerAccount account = ledgerAccountRepository.findById(e.accountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ledger account not found"));
            LedgerEntry entry = LedgerEntry.builder()
                    .id(UUID.randomUUID())
                    .journal(journal)
                    .account(account)
                    .debit(e.debit())
                    .credit(e.credit())
                    .currencyCode(e.currencyCode())
                    .build();
            ledgerEntryRepository.save(entry);
            applyWalletImpact(account, e.currencyCode(), e.debit(), e.credit());
        }

        return toResponse(journal);
    }

    @Transactional
    public LedgerJournalResponse reverseJournalProRata(UUID journalId) {
        LedgerJournal original = ledgerJournalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal not found"));
        if (original.getStatus() == LedgerJournalStatus.REVERSED) {
            throw new DomainException("Journal already reversed");
        }

        List<LedgerEntry> originalEntries = ledgerEntryRepository.findByJournalId(original.getId());
        if (originalEntries.isEmpty()) throw new DomainException("Journal has no entries");

        LedgerJournal reversal = ledgerJournalRepository.save(LedgerJournal.builder()
                .id(UUID.randomUUID())
                .referenceType(original.getReferenceType() + "_REVERSAL")
                .referenceId(original.getReferenceId())
                .status(LedgerJournalStatus.POSTED)
                .reversalOfJournalId(original.getId())
                .createdAt(OffsetDateTime.now())
                .build());

        for (LedgerEntry e : originalEntries) {
            LedgerEntry rev = LedgerEntry.builder()
                    .id(UUID.randomUUID())
                    .journal(reversal)
                    .account(e.getAccount())
                    .debit(e.getCredit())
                    .credit(e.getDebit())
                    .currencyCode(e.getCurrencyCode())
                    .build();
            ledgerEntryRepository.save(rev);
            applyWalletImpact(e.getAccount(), e.getCurrencyCode(), rev.getDebit(), rev.getCredit());
        }

        original.setStatus(LedgerJournalStatus.REVERSED);
        ledgerJournalRepository.save(original);
        return toResponse(reversal);
    }

    @Transactional(readOnly = true)
    public List<LedgerJournalResponse> journals() {
        return ledgerJournalRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toResponse).toList();
    }

    private void validateDoubleEntry(List<LedgerEntryRequest> entries) {
        BigDecimal totalDebit = entries.stream().map(LedgerEntryRequest::debit).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCredit = entries.stream().map(LedgerEntryRequest::credit).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new DomainException("Double-entry violation: total debit must equal total credit");
        }
    }

    private void applyWalletImpact(LedgerAccount account, String currencyCode, BigDecimal debit, BigDecimal credit) {
        if (account.getType() != LedgerAccountType.WALLET || account.getOwnerId() == null) return;

        Wallet wallet = walletRepository.findByUserId(account.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for account owner"));

        WalletBalance balance = walletBalanceRepository.findByWalletIdAndCurrencyCodeForUpdate(wallet.getId(), currencyCode)
                .orElseGet(() -> walletBalanceRepository.save(WalletBalance.builder()
                        .id(UUID.randomUUID())
                        .wallet(wallet)
                        .currencyCode(currencyCode)
                        .availableAmount(BigDecimal.ZERO)
                        .reservedAmount(BigDecimal.ZERO)
                        .build()));

        BigDecimal delta = credit.subtract(debit);
        BigDecimal next = balance.getAvailableAmount().add(delta);
        if (next.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Insufficient wallet funds for currency " + currencyCode);
        }
        balance.setAvailableAmount(next);
        walletBalanceRepository.save(balance);
    }

    private LedgerJournalResponse toResponse(LedgerJournal j) {
        return new LedgerJournalResponse(j.getId(), j.getReferenceType(), j.getReferenceId(), j.getStatus(), j.getReversalOfJournalId(), j.getCreatedAt());
    }
}
