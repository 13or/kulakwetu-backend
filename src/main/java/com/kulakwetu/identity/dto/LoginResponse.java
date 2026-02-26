package com.kulakwetu.identity.dto;

public record LoginResponse(String accessToken, long accessExpiresIn, String refreshToken, long refreshExpiresIn, boolean mfaRequired) {
}
