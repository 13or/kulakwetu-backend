package com.kulakwetu.agricash.dto;

import jakarta.validation.constraints.NotBlank;

public record WithdrawalDecisionRequest(String rejectionReason, @NotBlank String idempotencyKey) {}
