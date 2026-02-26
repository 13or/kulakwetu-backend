package com.kulakwetu.geo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ProvinceRequest(
        @NotNull UUID countryId,
        @NotBlank @Size(max = 120) String name,
        boolean isPublic
) {
}
