package com.kulakwetu.geo.repository;

import com.kulakwetu.geo.entity.Municipality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MunicipalityRepository extends JpaRepository<Municipality, UUID> {
    List<Municipality> findByIsPublicTrueAndCity_IdOrderByNameAsc(UUID cityId);
    Page<Municipality> findAllByOrderByNameAsc(Pageable pageable);
}
