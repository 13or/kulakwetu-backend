package com.kulakwetu.geo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "geo_municipality", uniqueConstraints = @UniqueConstraint(name = "uk_geo_municipality_city_name", columnNames = {"city_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Municipality {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;
}
