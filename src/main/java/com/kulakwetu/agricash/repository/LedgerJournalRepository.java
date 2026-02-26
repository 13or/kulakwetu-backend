package com.kulakwetu.agricash.repository;

import com.kulakwetu.agricash.entity.LedgerJournal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LedgerJournalRepository extends JpaRepository<LedgerJournal, UUID> {
    List<LedgerJournal> findAllByOrderByCreatedAtDesc();
}
