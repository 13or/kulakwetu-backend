package com.kulakwetu.reference.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ref_account_type", uniqueConstraints = @UniqueConstraint(name = "uk_ref_account_type_code", columnNames = "code"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountType {
    @Id
    private UUID id;

    @Column(nullable = false, length = 40)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
