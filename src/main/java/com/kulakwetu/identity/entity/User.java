package com.kulakwetu.identity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "id_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "phone_number", nullable = false, unique = true, length = 30)
    private String phoneNumber;

    @Column(unique = true, length = 120)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @Column(name = "account_type", nullable = false, length = 30)
    private String accountType;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "verified", nullable = false)
    private boolean verified;

    @Column(name = "mfa_enabled", nullable = false)
    private boolean mfaEnabled;

    @Column(name = "mfa_secret", length = 128)
    private String mfaSecret;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
