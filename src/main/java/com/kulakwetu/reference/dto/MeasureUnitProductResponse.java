package com.kulakwetu.reference.dto;

import java.util.UUID;

public record MeasureUnitProductResponse(UUID id, String name, String symbol, boolean isPublic) {}
