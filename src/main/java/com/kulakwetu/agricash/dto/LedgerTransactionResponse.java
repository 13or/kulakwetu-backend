package com.kulakwetu.agricash.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record LedgerTransactionResponse(
        UUID journalId,
        String referenceType,
        UUID referenceId,
        OffsetDateTime createdAt,
        BigDecimal debit,
        BigDecimal credit,
        String currencyCode
) {}
