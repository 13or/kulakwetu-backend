package com.kulakwetu.geo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "geo_country", uniqueConstraints = {
        @UniqueConstraint(name = "uk_geo_country_name", columnNames = "name"),
        @UniqueConstraint(name = "uk_geo_country_iso", columnNames = "iso_code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {
    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "iso_code", nullable = false, length = 3)
    private String isoCode;

    @Column(name = "phone_code", nullable = false, length = 10)
    private String phoneCode;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;
}
