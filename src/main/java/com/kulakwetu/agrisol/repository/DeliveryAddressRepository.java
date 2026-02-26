package com.kulakwetu.agrisol.repository;

import com.kulakwetu.agrisol.entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, UUID> {
    Optional<DeliveryAddress> findByOrderId(UUID orderId);
}
