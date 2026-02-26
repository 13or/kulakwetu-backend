package com.kulakwetu.admin.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record SubscriptionTypeRequest(
        @NotBlank @Size(max = 120) String name,
        String benefits,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        @NotBlank @Size(min = 3, max = 3) String currencyCode,
        @NotNull Integer durationDays,
        boolean isActive
) {
}
