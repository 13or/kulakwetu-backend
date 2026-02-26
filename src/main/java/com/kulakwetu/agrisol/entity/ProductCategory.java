package com.kulakwetu.agrisol.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ag_product_category", uniqueConstraints = @UniqueConstraint(name = "uk_ag_product_category_name", columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory {
    @Id
    private UUID id;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;
}
