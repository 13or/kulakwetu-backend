package com.kulakwetu.agrisol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SupplierBusinessRequest(
        @NotBlank @Size(max = 160) String businessName,
        @Size(max = 60) String idnat,
        @Size(max = 60) String rccm,
        @Size(max = 60) String taxNumber,
        @Size(max = 255) String locationAddress,
        @Size(max = 255) String productionAddress,
        @Size(max = 255) String logo,
        @NotBlank @Size(max = 30) String phone,
        @Size(max = 30) String whatsapp,
        @Size(max = 120) String email,
        @Size(max = 255) String facebook,
        @Size(max = 255) String linkedin,
        @Size(max = 255) String instagram,
        @Size(max = 255) String tiktok,
        @Size(max = 255) String youtube
) {}
