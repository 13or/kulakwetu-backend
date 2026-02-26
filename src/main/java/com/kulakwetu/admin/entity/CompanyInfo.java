package com.kulakwetu.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "adm_company_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyInfo {

    @Id
    private UUID id;

    @Column(name = "legal_name", nullable = false, length = 140)
    private String legalName;

    @Column(name = "trade_name", length = 140)
    private String tradeName;

    @Column(name = "support_email", length = 120)
    private String supportEmail;

    @Column(name = "support_phone", length = 30)
    private String supportPhone;

    @Column(name = "website_url", length = 255)
    private String websiteUrl;

    @Column(name = "agrisol_description", columnDefinition = "TEXT")
    private String agrisolDescription;

    @Column(name = "agricash_description", columnDefinition = "TEXT")
    private String agricashDescription;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
