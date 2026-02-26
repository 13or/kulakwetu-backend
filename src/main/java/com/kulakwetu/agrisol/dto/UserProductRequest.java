package com.kulakwetu.agrisol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UserProductRequest(
        @NotNull UUID agrisolProductId,
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 500) String description,
        boolean inStock,
        BigDecimal quantityInStock,
        @Size(max = 40) String stockMeasureUnit,
        boolean available,
        OffsetDateTime availableAt,
        BigDecimal productionSpace,
        @Size(max = 40) String productionSpaceUnit,
        @Size(max = 255) String productionLocation,
        boolean isPopular
) {}
