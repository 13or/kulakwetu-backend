package com.kulakwetu.agricash.entity;

import com.kulakwetu.agricash.enums.PaymentIntentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ag_payment_intent", uniqueConstraints = {
        @UniqueConstraint(name = "uk_ag_payment_intent_order", columnNames = "order_id"),
        @UniqueConstraint(name = "uk_ag_payment_intent_idempotency", columnNames = "idempotency_key")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentIntent {
    @Id
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "payer_user_id", nullable = false)
    private UUID payerUserId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentIntentStatus status;

    @Column(name = "captured_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal capturedAmount;

    @Column(name = "refunded_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal refundedAmount;

    @Column(name = "idempotency_key", nullable = false, length = 120)
    private String idempotencyKey;

    @Column(name = "provider_ref", length = 120)
    private String providerRef;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Version
    @Column(nullable = false)
    private Long version;
}
