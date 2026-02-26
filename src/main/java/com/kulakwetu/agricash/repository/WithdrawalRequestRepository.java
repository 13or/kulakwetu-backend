package com.kulakwetu.agricash.repository;

import com.kulakwetu.agricash.entity.WithdrawalRequest;
import com.kulakwetu.agricash.enums.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequest, UUID> {
    Optional<WithdrawalRequest> findByIdempotencyKey(String idempotencyKey);
    Optional<WithdrawalRequest> findTopBySupplierUserIdOrderByCreatedAtDesc(UUID supplierUserId);
    List<WithdrawalRequest> findBySupplierUserIdOrderByCreatedAtDesc(UUID supplierUserId);
    List<WithdrawalRequest> findByStatusOrderByCreatedAtDesc(WithdrawalStatus status);

    @Query("select coalesce(sum(w.amount),0) from WithdrawalRequest w where w.supplierUserId = :supplierUserId and w.status in :statuses and w.createdAt between :from and :to")
    BigDecimal sumToday(UUID supplierUserId, List<WithdrawalStatus> statuses, OffsetDateTime from, OffsetDateTime to);

    @Query("select max(w.createdAt) from WithdrawalRequest w where w.supplierUserId = :supplierUserId")
    OffsetDateTime lastRequestAt(UUID supplierUserId);
}
