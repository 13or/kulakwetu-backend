package com.kulakwetu.geo.dto;

import java.util.UUID;

public record MunicipalityResponse(UUID id, UUID cityId, String name, boolean isPublic) {
}
