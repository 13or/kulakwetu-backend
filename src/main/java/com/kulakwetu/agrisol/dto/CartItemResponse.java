package com.kulakwetu.agrisol.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(UUID itemId, UUID userProductId, String productName, BigDecimal quantity) {}
