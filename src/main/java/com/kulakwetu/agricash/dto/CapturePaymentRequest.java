package com.kulakwetu.agricash.dto;

import jakarta.validation.constraints.NotBlank;

public record CapturePaymentRequest(@NotBlank String idempotencyKey) {}
