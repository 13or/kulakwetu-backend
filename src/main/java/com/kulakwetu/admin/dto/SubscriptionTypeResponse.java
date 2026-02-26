package com.kulakwetu.admin.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record SubscriptionTypeResponse(
        UUID id,
        String name,
        String benefits,
        BigDecimal price,
        String currencyCode,
        Integer durationDays,
        boolean isActive,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
