package com.kulakwetu.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MfaConfirmRequest(@NotBlank @Pattern(regexp = "^[0-9]{6}$") String code) {
}
