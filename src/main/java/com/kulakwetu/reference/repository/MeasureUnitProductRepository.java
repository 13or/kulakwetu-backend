package com.kulakwetu.reference.repository;

import com.kulakwetu.reference.entity.MeasureUnitProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MeasureUnitProductRepository extends JpaRepository<MeasureUnitProduct, UUID> {
}
