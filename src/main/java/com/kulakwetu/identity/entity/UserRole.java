package com.kulakwetu.identity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "id_user_role", uniqueConstraints = @UniqueConstraint(name = "uk_id_user_role", columnNames = {"user_id", "role_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
