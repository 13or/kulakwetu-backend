package com.kulakwetu.agrisol.dto;

import com.kulakwetu.agrisol.enums.OrderStatus;
import com.kulakwetu.agrisol.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderSummaryResponse(
        UUID id,
        OrderStatus status,
        PaymentMethod paymentMethod,
        String currencyCode,
        BigDecimal subtotal,
        BigDecimal deliveryFee,
        BigDecimal total,
        UUID assignedDriverUserId
) {}
