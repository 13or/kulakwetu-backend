package com.kulakwetu.agricash.repository;

import com.kulakwetu.agricash.entity.PaymentIntent;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PaymentIntentRepository extends JpaRepository<PaymentIntent, UUID> {
    boolean existsByOrderId(UUID orderId);
    Optional<PaymentIntent> findByOrderId(UUID orderId);
    Optional<PaymentIntent> findByIdempotencyKey(String idempotencyKey);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PaymentIntent p where p.id = :id")
    Optional<PaymentIntent> lockById(UUID id);
}
