package com.kulakwetu.geo.repository;

import com.kulakwetu.geo.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {
    List<City> findByIsPublicTrueAndProvince_IdOrderByNameAsc(UUID provinceId);
    Page<City> findAllByOrderByNameAsc(Pageable pageable);
}
