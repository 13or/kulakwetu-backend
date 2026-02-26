package com.kulakwetu.agrisol.dto;

import com.kulakwetu.agrisol.enums.UserProductStatus;

import java.util.UUID;

public record UserProductResponse(UUID id, UUID supplierUserId, UUID agrisolProductId, String name, String description, UserProductStatus status) {}
