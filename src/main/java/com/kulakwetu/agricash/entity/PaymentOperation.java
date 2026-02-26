package com.kulakwetu.agricash.entity;

import com.kulakwetu.agricash.enums.PaymentOperationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ag_payment_operation", uniqueConstraints = @UniqueConstraint(name = "uk_ag_payment_op_type_key", columnNames = {"operation_type", "idempotency_key"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentOperation {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_intent_id", nullable = false)
    private PaymentIntent paymentIntent;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false, length = 30)
    private PaymentOperationType operationType;

    @Column(name = "idempotency_key", nullable = false, length = 120)
    private String idempotencyKey;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
