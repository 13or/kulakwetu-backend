package com.kulakwetu.agricash.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WithdrawalCreateRequest(
        @NotNull @DecimalMin("0.0001") BigDecimal amount,
        @NotBlank String currencyCode,
        @NotBlank String idempotencyKey,
        @NotBlank String payoutMethod,
        @NotBlank String payoutAccount
) {}
