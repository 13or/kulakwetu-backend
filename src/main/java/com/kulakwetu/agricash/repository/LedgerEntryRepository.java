package com.kulakwetu.agricash.repository;

import com.kulakwetu.agricash.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {
    List<LedgerEntry> findByJournalId(UUID journalId);
    List<LedgerEntry> findByAccountOwnerIdOrderByJournalCreatedAtDesc(UUID ownerId);
}
