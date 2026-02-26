package com.kulakwetu.agrisol.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemRequest(
        @NotNull UUID userProductId,
        @NotNull @DecimalMin(value = "0.0001") BigDecimal quantity
) {}
