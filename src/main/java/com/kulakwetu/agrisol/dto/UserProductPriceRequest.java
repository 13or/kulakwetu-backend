package com.kulakwetu.agrisol.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UserProductPriceRequest(
        @NotBlank @Size(max = 40) String unit,
        @NotNull @DecimalMin("0.0001") BigDecimal price,
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String currencyCode
) {}
