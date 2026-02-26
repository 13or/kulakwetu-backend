package com.kulakwetu.agrisol.entity;

import com.kulakwetu.agrisol.enums.CartStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ag_cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CartStatus status;
}
