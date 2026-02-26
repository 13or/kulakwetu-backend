package com.kulakwetu.reference.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ref_currency", uniqueConstraints = @UniqueConstraint(name = "uk_ref_currency_code", columnNames = "code"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {
    @Id
    private UUID id;

    @Column(nullable = false, length = 3)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 10)
    private String symbol;

    @Column(nullable = false)
    private int decimals;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
