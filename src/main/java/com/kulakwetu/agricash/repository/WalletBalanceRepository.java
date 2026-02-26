package com.kulakwetu.agricash.repository;

import com.kulakwetu.agricash.entity.WalletBalance;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletBalanceRepository extends JpaRepository<WalletBalance, UUID> {
    List<WalletBalance> findByWalletIdOrderByCurrencyCodeAsc(UUID walletId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select wb from WalletBalance wb where wb.wallet.id = :walletId and wb.currencyCode = :currencyCode")
    Optional<WalletBalance> findByWalletIdAndCurrencyCodeForUpdate(UUID walletId, String currencyCode);
}
