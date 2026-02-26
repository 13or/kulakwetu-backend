package com.kulakwetu.agricash.dto;

import com.kulakwetu.agricash.enums.WalletStatus;

import java.util.UUID;

public record WalletResponse(UUID walletId, UUID userId, WalletStatus status) {}
