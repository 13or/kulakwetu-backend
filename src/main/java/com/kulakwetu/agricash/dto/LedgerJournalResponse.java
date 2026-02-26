package com.kulakwetu.agricash.dto;

import com.kulakwetu.agricash.enums.LedgerJournalStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LedgerJournalResponse(
        UUID journalId,
        String referenceType,
        UUID referenceId,
        LedgerJournalStatus status,
        UUID reversalOfJournalId,
        OffsetDateTime createdAt
) {}
