package com.kulakwetu.agrisol.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ShopProductResponse(UUID userProductId, String name, String description, String baseProductName, List<String> mediaUrls, List<PriceView> prices) {
    public record PriceView(String unit, BigDecimal price, String currencyCode) {}
}
