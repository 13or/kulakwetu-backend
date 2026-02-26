package com.kulakwetu.geo.dto;

import java.util.UUID;

public record ProvinceResponse(UUID id, UUID countryId, String name, boolean isPublic) {
}
