package com.kulakwetu.reference.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MeasureUnitLandRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 20) String symbol,
        boolean isPublic
) {}
