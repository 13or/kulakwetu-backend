package com.kulakwetu.identity.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyAccountRequest(@NotBlank String token) {
}
