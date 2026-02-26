package com.kulakwetu.geo.dto;

import java.util.UUID;

public record CountryResponse(UUID id, String name, String isoCode, String phoneCode, boolean isPublic) {
}
