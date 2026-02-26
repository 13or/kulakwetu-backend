package com.kulakwetu.identity.dto;

import java.util.UUID;

public record RegisterResponse(UUID userId, UUID walletId, String verificationToken) {
}
