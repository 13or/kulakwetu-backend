package com.kulakwetu.admin.dto;

import com.kulakwetu.admin.enums.SlideTarget;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SlideRequest(
        @NotNull SlideTarget target,
        @NotBlank @Size(max = 255) String imageUrl,
        @NotBlank @Size(max = 160) String heading,
        @Size(max = 255) String text,
        String description,
        @NotBlank @Size(max = 160) String slug,
        boolean isPublic
) {
}
