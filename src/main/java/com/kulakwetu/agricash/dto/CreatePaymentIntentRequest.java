package com.kulakwetu.agricash.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePaymentIntentRequest(
        @NotNull UUID orderId,
        @NotBlank String idempotencyKey
) {}
