package com.kulakwetu.identity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "id_refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, unique = true, length = 140)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
