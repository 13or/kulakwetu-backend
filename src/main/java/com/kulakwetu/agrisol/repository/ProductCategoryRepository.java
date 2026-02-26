package com.kulakwetu.agrisol.repository;

import com.kulakwetu.agrisol.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
    List<ProductCategory> findByIsPublicTrueOrderByNameAsc();
}
