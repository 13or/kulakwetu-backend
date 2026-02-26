package com.kulakwetu.agrisol.entity;

import com.kulakwetu.agrisol.enums.UserProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ag_user_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProduct {
    @Id
    private UUID id;

    @Column(name = "supplier_user_id", nullable = false)
    private UUID supplierUserId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agrisol_product_id", nullable = false)
    private AgrisolProduct agrisolProduct;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(name = "in_stock", nullable = false)
    private boolean inStock;

    @Column(name = "quantity_in_stock", precision = 19, scale = 4)
    private BigDecimal quantityInStock;

    @Column(name = "reserved_quantity", nullable = false, precision = 19, scale = 4)
    private BigDecimal reservedQuantity;

    @Column(name = "stock_measure_unit", length = 40)
    private String stockMeasureUnit;

    @Column(nullable = false)
    private boolean available;

    @Column(name = "available_at")
    private OffsetDateTime availableAt;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "production_space", precision = 19, scale = 4)
    private BigDecimal productionSpace;

    @Column(name = "production_space_unit", length = 40)
    private String productionSpaceUnit;

    @Column(name = "production_location", length = 255)
    private String productionLocation;

    @Column(name = "is_popular", nullable = false)
    private boolean isPopular;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserProductStatus status;
}
