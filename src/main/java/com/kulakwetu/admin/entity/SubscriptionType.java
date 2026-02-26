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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "adm_subscription_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionType {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
