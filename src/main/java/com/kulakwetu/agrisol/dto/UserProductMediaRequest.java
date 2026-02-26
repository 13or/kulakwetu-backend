package com.kulakwetu.agrisol.dto;

import com.kulakwetu.agrisol.enums.MediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserProductMediaRequest(
        @NotBlank @Size(max = 255) String mediaUrl,
        boolean featured,
        @NotNull MediaType type
) {}
