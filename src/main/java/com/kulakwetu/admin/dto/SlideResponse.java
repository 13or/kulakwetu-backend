package com.kulakwetu.admin.dto;

import com.kulakwetu.admin.enums.SlideTarget;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SlideResponse(
        UUID id,
        SlideTarget target,
        String imageUrl,
        String heading,
        String text,
        String description,
        String slug,
        boolean isPublic,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
