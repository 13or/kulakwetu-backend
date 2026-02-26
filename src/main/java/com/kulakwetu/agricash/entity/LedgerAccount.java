package com.kulakwetu.agricash.entity;

import com.kulakwetu.agricash.enums.LedgerAccountType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ag_ledger_account", uniqueConstraints = @UniqueConstraint(name = "uk_ag_ledger_account_type_owner_currency", columnNames = {"type", "owner_id", "currency_code"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerAccount {
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private LedgerAccountType type;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;
}
