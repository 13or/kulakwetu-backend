package com.kulakwetu.agricash.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record LedgerEntryRequest(
        @NotNull UUID accountId,
        @NotNull @DecimalMin("0.0000") BigDecimal debit,
        @NotNull @DecimalMin("0.0000") BigDecimal credit,
        @NotBlank String currencyCode
) {}
