package com.kulakwetu.agrisol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AgrisolProductRequest(
        @Size(max = 255) String imageUrl,
        @NotBlank @Size(max = 120) String name,
        @NotNull UUID productCategoryId,
        boolean isPublic,
        boolean isPopular
) {}
