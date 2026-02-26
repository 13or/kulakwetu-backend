package com.kulakwetu.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanyInfoRequest(
        @NotBlank @Size(max = 140) String legalName,
        @Size(max = 140) String tradeName,
        @Size(max = 120) String supportEmail,
        @Size(max = 30) String supportPhone,
        @Size(max = 255) String websiteUrl,
        String agrisolDescription,
        String agricashDescription
) {
}
