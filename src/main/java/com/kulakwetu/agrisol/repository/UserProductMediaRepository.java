package com.kulakwetu.agrisol.repository;

import com.kulakwetu.agrisol.entity.UserProductMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserProductMediaRepository extends JpaRepository<UserProductMedia, UUID> {
    List<UserProductMedia> findByUserProductId(UUID userProductId);
}
