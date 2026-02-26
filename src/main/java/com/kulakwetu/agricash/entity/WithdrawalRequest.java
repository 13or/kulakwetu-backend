package com.kulakwetu.agricash.entity;

import com.kulakwetu.agricash.enums.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ag_withdrawal_request", uniqueConstraints = @UniqueConstraint(name = "uk_ag_withdrawal_idempotency", columnNames = "idempotency_key"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawalRequest {
    @Id
    private UUID id;

    @Column(name = "supplier_user_id", nullable = false)
    private UUID supplierUserId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WithdrawalStatus status;

    @Column(name = "idempotency_key", nullable = false, length = 120)
    private String idempotencyKey;

    @Column(name = "payout_method", nullable = false, length = 40)
    private String payoutMethod;

    @Column(name = "payout_account", nullable = false, length = 120)
    private String payoutAccount;

    @Column(name = "rejection_reason", length = 255)
    private String rejectionReason;

    @Column(name = "external_reference", length = 120)
    private String externalReference;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
