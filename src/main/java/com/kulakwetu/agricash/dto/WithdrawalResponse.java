package com.kulakwetu.agricash.dto;

import com.kulakwetu.agricash.enums.WithdrawalStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawalResponse(
        UUID id,
        UUID supplierUserId,
        BigDecimal amount,
        String currencyCode,
        WithdrawalStatus status,
        String payoutMethod,
        String payoutAccount,
        String rejectionReason,
        String externalReference
) {}
