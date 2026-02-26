package com.kulakwetu.reference.repository;

import com.kulakwetu.reference.entity.MeasureUnitLand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MeasureUnitLandRepository extends JpaRepository<MeasureUnitLand, UUID> {
}
