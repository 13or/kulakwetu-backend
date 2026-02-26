package com.kulakwetu.geo.dto;

import java.util.UUID;

public record CityResponse(UUID id, UUID provinceId, String name, boolean isPublic) {
}
