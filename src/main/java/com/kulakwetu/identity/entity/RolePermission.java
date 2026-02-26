package com.kulakwetu.identity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "id_role_permission", uniqueConstraints = @UniqueConstraint(name = "uk_id_role_permission", columnNames = {"role_id", "permission_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {
    @Id
    private UUID id;

    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Column(name = "permission_id", nullable = false)
    private UUID permissionId;
}
