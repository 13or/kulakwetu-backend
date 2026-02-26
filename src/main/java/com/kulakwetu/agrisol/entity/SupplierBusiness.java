package com.kulakwetu.agrisol.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ag_supplier_business", uniqueConstraints = @UniqueConstraint(name = "uk_ag_supplier_business_user", columnNames = "supplier_user_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierBusiness {
    @Id
    private UUID id;

    @Column(name = "supplier_user_id", nullable = false)
    private UUID supplierUserId;

    @Column(name = "business_name", nullable = false, length = 160)
    private String businessName;

    @Column(length = 60)
    private String idnat;

    @Column(length = 60)
    private String rccm;

    @Column(name = "tax_number", length = 60)
    private String taxNumber;

    @Column(name = "location_address", length = 255)
    private String locationAddress;

    @Column(name = "production_address", length = 255)
    private String productionAddress;

    @Column(name = "logo_url", length = 255)
    private String logo;

    @Column(name = "phone_number", nullable = false, length = 30)
    private String phone;

    @Column(length = 30)
    private String whatsapp;

    @Column(length = 120)
    private String email;

    @Column(length = 255)
    private String facebook;

    @Column(length = 255)
    private String linkedin;

    @Column(length = 255)
    private String instagram;

    @Column(length = 255)
    private String tiktok;

    @Column(length = 255)
    private String youtube;
}
