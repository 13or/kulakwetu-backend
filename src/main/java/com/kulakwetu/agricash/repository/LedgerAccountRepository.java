package com.kulakwetu.agricash.repository;

import com.kulakwetu.agricash.entity.LedgerAccount;
import com.kulakwetu.agricash.enums.LedgerAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, UUID> {
    Optional<LedgerAccount> findByTypeAndOwnerIdAndCurrencyCode(LedgerAccountType type, UUID ownerId, String currencyCode);
    Optional<LedgerAccount> findByTypeAndCurrencyCodeAndOwnerIdIsNull(LedgerAccountType type, String currencyCode);
}
