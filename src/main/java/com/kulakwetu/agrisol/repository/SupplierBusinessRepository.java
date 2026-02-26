package com.kulakwetu.agrisol.repository;

import com.kulakwetu.agrisol.entity.SupplierBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SupplierBusinessRepository extends JpaRepository<SupplierBusiness, UUID> {
    Optional<SupplierBusiness> findBySupplierUserId(UUID supplierUserId);
}
