package com.kulakwetu.reference.dto;

import java.util.UUID;

public record AccountTypeResponse(UUID id, String code, String name, boolean isActive) {}
