package com.kulakwetu.agrisol.repository;

import com.kulakwetu.agrisol.entity.Cart;
import com.kulakwetu.agrisol.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserIdAndStatus(UUID userId, CartStatus status);
}
