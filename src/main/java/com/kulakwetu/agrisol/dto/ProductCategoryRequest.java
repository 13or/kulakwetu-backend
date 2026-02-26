package com.kulakwetu.agrisol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductCategoryRequest(
        @Size(max = 255) String imageUrl,
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 500) String description,
        boolean isPublic
) {}
