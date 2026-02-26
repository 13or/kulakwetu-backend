package com.kulakwetu.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MfaVerifyRequest(
        @NotBlank String identifier,
        @NotBlank String password,
        boolean rememberMe,
        @NotBlank @Pattern(regexp = "^[0-9]{6}$") String code
) {
}
