package com.kulakwetu.agrisol.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ag_cart_item", uniqueConstraints = @UniqueConstraint(name = "uk_ag_cart_item_cart_product", columnNames = {"cart_id", "user_product_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_product_id", nullable = false)
    private UserProduct userProduct;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;
}
