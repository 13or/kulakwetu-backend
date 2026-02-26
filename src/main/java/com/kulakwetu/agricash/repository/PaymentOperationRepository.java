package com.kulakwetu.agricash.repository;

import com.kulakwetu.agricash.entity.PaymentOperation;
import com.kulakwetu.agricash.enums.PaymentOperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentOperationRepository extends JpaRepository<PaymentOperation, UUID> {
    Optional<PaymentOperation> findByOperationTypeAndIdempotencyKey(PaymentOperationType operationType, String idempotencyKey);
}
