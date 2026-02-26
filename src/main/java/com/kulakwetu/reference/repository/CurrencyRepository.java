package com.kulakwetu.reference.repository;

import com.kulakwetu.reference.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currency, UUID> {
}
