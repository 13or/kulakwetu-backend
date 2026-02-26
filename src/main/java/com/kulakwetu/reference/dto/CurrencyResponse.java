package com.kulakwetu.reference.dto;

import java.util.UUID;

public record CurrencyResponse(UUID id, String code, String name, String symbol, int decimals, boolean isActive) {}
