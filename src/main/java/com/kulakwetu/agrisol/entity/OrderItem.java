package com.kulakwetu.agrisol.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ag_order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_product_id", nullable = false)
    private UserProduct userProduct;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "line_total", nullable = false, precision = 19, scale = 4)
    private BigDecimal lineTotal;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;
}
