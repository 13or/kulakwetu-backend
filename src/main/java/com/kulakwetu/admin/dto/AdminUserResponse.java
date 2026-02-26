package com.kulakwetu.admin.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AdminUserResponse(
        UUID id,
        String username,
        String phoneNumber,
        String email,
        String firstName,
        String lastName,
        String accountType,
        boolean enabled,
        boolean verified,
        OffsetDateTime createdAt
) {
}
