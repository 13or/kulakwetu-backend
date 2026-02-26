package com.kulakwetu.agricash.dto;

import com.kulakwetu.agricash.enums.PaymentIntentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentIntentResponse(
        UUID id,
        UUID orderId,
        UUID payerUserId,
        BigDecimal amount,
        String currencyCode,
        PaymentIntentStatus status,
        BigDecimal capturedAmount,
        BigDecimal refundedAmount,
        String idempotencyKey,
        String providerRef
) {}
