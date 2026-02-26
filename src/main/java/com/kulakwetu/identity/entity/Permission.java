package com.kulakwetu.identity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "id_permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;
}
