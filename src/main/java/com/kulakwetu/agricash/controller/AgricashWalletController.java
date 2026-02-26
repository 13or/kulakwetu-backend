package com.kulakwetu.agricash.controller;

import com.kulakwetu.agricash.dto.LedgerTransactionResponse;
import com.kulakwetu.agricash.dto.WalletBalanceResponse;
import com.kulakwetu.agricash.dto.WalletResponse;
import com.kulakwetu.agricash.service.WalletService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/agricash/wallet")
@RequiredArgsConstructor
@Tag(name = "AGRICASH - WALLET")
@PreAuthorize("isAuthenticated()")
public class AgricashWalletController {

    private final WalletService walletService;

    @GetMapping("/me")
    public WalletResponse me() { return walletService.myWallet(); }

    @GetMapping("/balances")
    public List<WalletBalanceResponse> balances() { return walletService.myBalances(); }

    @GetMapping("/transactions")
    public List<LedgerTransactionResponse> transactions() { return walletService.myTransactions(); }
}
