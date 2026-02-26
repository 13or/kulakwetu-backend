package com.kulakwetu.geo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CountryRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Pattern(regexp = "^[A-Z]{2,3}$") String isoCode,
        @NotBlank @Pattern(regexp = "^\\+?[0-9]{1,6}$") String phoneCode,
        boolean isPublic
) {
}
