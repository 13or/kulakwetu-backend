package com.kulakwetu.geo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "geo_city", uniqueConstraints = @UniqueConstraint(name = "uk_geo_city_province_name", columnNames = {"province_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;
}
