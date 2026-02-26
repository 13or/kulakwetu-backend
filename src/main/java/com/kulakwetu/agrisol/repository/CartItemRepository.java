package com.kulakwetu.agrisol.repository;

import com.kulakwetu.agrisol.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByCartId(UUID cartId);
    Optional<CartItem> findByCartIdAndUserProductId(UUID cartId, UUID userProductId);
    void deleteByCartId(UUID cartId);
}
