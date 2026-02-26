package com.kulakwetu.agricash.entity;

import com.kulakwetu.agricash.enums.LedgerJournalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ag_ledger_journal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerJournal {
    @Id
    private UUID id;

    @Column(name = "reference_type", nullable = false, length = 60)
    private String referenceType;

    @Column(name = "reference_id", nullable = false)
    private UUID referenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LedgerJournalStatus status;

    @Column(name = "reversal_of_journal_id")
    private UUID reversalOfJournalId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
