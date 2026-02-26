package com.kulakwetu.geo.repository;

import com.kulakwetu.geo.entity.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProvinceRepository extends JpaRepository<Province, UUID> {
    List<Province> findByIsPublicTrueAndCountry_IdOrderByNameAsc(UUID countryId);
    Page<Province> findAllByOrderByNameAsc(Pageable pageable);
}
