package com.kulakwetu.agricash.entity;

import com.kulakwetu.agricash.enums.WalletStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ag_wallet", uniqueConstraints = @UniqueConstraint(name = "uk_ag_wallet_user", columnNames = "user_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WalletStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
