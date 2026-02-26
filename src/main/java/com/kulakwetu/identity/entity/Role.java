package com.kulakwetu.identity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "id_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;
}
