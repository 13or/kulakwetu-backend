package com.kulakwetu.agrisol.entity;

import com.kulakwetu.agrisol.enums.OrderStatus;
import com.kulakwetu.agrisol.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ag_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal subtotal;

    @Column(name = "delivery_fee", nullable = false, precision = 19, scale = 4)
    private BigDecimal deliveryFee;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal total;

    @Column(name = "assigned_driver_user_id")
    private UUID assignedDriverUserId;
}
