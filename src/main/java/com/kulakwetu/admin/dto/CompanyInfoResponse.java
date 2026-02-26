package com.kulakwetu.admin.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CompanyInfoResponse(
        UUID id,
        String legalName,
        String tradeName,
        String supportEmail,
        String supportPhone,
        String websiteUrl,
        String agrisolDescription,
        String agricashDescription,
        OffsetDateTime updatedAt
) {
}
