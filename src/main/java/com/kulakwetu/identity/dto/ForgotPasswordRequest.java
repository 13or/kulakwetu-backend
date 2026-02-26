package com.kulakwetu.identity.dto;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(@NotBlank String identifier) {
}
