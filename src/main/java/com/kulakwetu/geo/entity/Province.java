package com.kulakwetu.geo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "geo_province", uniqueConstraints = @UniqueConstraint(name = "uk_geo_province_country_name", columnNames = {"country_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Province {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;
}
