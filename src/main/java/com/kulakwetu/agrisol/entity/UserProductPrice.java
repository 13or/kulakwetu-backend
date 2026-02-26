package com.kulakwetu.agrisol.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ag_user_product_price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProductPrice {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_product_id", nullable = false)
    private UserProduct userProduct;

    @Column(nullable = false, length = 40)
    private String unit;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;
}
