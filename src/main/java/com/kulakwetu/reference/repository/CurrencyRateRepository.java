package com.kulakwetu.reference.repository;

import com.kulakwetu.reference.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, UUID> {
}
