package com.kulakwetu.admin.repository;

import com.kulakwetu.admin.entity.Slide;
import com.kulakwetu.admin.enums.SlideTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SlideRepository extends JpaRepository<Slide, UUID> {
    Page<Slide> findByTargetOrderByCreatedAtDesc(SlideTarget target, Pageable pageable);
}
