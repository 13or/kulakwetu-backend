package com.kulakwetu.reference.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CurrencyRateRequest(
        @NotNull UUID baseCurrencyId,
        @NotNull UUID quoteCurrencyId,
        @NotNull @DecimalMin(value = "0.00000001") BigDecimal rate,
        @NotNull OffsetDateTime validFrom
) {}
