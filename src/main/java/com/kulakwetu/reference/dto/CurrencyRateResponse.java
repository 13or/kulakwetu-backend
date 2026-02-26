package com.kulakwetu.reference.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CurrencyRateResponse(UUID id, UUID baseCurrencyId, UUID quoteCurrencyId, BigDecimal rate, OffsetDateTime validFrom) {}
