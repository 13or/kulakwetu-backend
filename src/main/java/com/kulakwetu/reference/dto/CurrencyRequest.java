package com.kulakwetu.reference.dto;

import jakarta.validation.constraints.*;

public record CurrencyRequest(
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String code,
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 10) String symbol,
        @Min(0) @Max(8) int decimals,
        boolean isActive
) {}
