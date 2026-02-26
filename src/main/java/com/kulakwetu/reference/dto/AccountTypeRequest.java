package com.kulakwetu.reference.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountTypeRequest(
        @NotBlank @Size(max = 40) String code,
        @NotBlank @Size(max = 120) String name,
        boolean isActive
) {}
