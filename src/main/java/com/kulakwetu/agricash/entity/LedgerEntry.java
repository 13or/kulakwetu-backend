package com.kulakwetu.agricash.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ag_ledger_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerEntry {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "journal_id", nullable = false)
    private LedgerJournal journal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private LedgerAccount account;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal debit;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal credit;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;
}
