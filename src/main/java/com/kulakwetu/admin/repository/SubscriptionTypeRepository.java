package com.kulakwetu.admin.repository;

import com.kulakwetu.admin.entity.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionType, UUID> {
}
