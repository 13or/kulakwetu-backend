package com.kulakwetu.agrisol.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ag_product", uniqueConstraints = @UniqueConstraint(name = "uk_ag_product_name_category", columnNames = {"name", "product_category_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgrisolProduct {
    @Id
    private UUID id;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(nullable = false, length = 120)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_category_id", nullable = false)
    private ProductCategory productCategory;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "is_popular", nullable = false)
    private boolean isPopular;
}
