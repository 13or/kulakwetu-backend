package com.kulakwetu.reference.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ref_account_category", uniqueConstraints = @UniqueConstraint(name = "uk_ref_account_category_code", columnNames = "code"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCategory {
    @Id
    private UUID id;

    @Column(nullable = false, length = 40)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
