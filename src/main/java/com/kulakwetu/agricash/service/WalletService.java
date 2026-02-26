package com.kulakwetu.agricash.service;

import com.kulakwetu.agricash.dto.LedgerTransactionResponse;
import com.kulakwetu.agricash.dto.WalletBalanceResponse;
import com.kulakwetu.agricash.dto.WalletResponse;
import com.kulakwetu.agricash.entity.Wallet;
import com.kulakwetu.agricash.entity.WalletBalance;
import com.kulakwetu.agricash.repository.LedgerEntryRepository;
import com.kulakwetu.agricash.repository.WalletBalanceRepository;
import com.kulakwetu.agricash.repository.WalletRepository;
import com.kulakwetu.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletBalanceRepository walletBalanceRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    @Transactional(readOnly = true)
    public WalletResponse myWallet() {
        Wallet wallet = getMyWallet();
        return new WalletResponse(wallet.getId(), wallet.getUserId(), wallet.getStatus());
    }

    @Transactional(readOnly = true)
    public List<WalletBalanceResponse> myBalances() {
        Wallet wallet = getMyWallet();
        List<WalletBalance> balances = walletBalanceRepository.findByWalletIdOrderByCurrencyCodeAsc(wallet.getId());
        return balances.stream().map(b -> new WalletBalanceResponse(b.getCurrencyCode(), b.getAvailableAmount(), b.getReservedAmount())).toList();
    }

    @Transactional(readOnly = true)
    public List<LedgerTransactionResponse> myTransactions() {
        UUID userId = currentUserId();
        return ledgerEntryRepository.findByAccountOwnerIdOrderByJournalCreatedAtDesc(userId).stream()
                .map(e -> new LedgerTransactionResponse(
                        e.getJournal().getId(),
                        e.getJournal().getReferenceType(),
                        e.getJournal().getReferenceId(),
                        e.getJournal().getCreatedAt(),
                        e.getDebit(),
                        e.getCredit(),
                        e.getCurrencyCode()
                )).toList();
    }

    private Wallet getMyWallet() {
        return walletRepository.findByUserId(currentUserId()).orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
    }

    private UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(auth.getName());
    }
}
