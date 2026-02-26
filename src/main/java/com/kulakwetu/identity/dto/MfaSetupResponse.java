package com.kulakwetu.identity.dto;

public record MfaSetupResponse(String secret, String qrBase64) {
}
