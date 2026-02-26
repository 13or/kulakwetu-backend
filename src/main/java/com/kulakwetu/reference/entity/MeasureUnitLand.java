package com.kulakwetu.reference.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ref_measure_unit_land", uniqueConstraints = @UniqueConstraint(name = "uk_ref_unit_land_name", columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeasureUnitLand {
    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 20)
    private String symbol;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;
}
