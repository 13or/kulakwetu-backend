package com.kulakwetu.geo.repository;

import com.kulakwetu.geo.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<Country, UUID> {
    List<Country> findByIsPublicTrueOrderByNameAsc();
    Page<Country> findAllByOrderByNameAsc(Pageable pageable);
}
