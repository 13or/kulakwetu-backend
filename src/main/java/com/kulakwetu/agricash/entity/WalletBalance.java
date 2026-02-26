package com.kulakwetu.agricash.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ag_wallet_balance", uniqueConstraints = @UniqueConstraint(name = "uk_ag_wallet_balance_wallet_currency", columnNames = {"wallet_id", "currency_code"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletBalance {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "available_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal availableAmount;

    @Column(name = "reserved_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal reservedAmount;

    @Version
    @Column(nullable = false)
    private Long version;
}
