package com.kulakwetu.agrisol.repository;

import com.kulakwetu.agrisol.entity.UserProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserProductPriceRepository extends JpaRepository<UserProductPrice, UUID> {
    List<UserProductPrice> findByUserProductId(UUID userProductId);
}
