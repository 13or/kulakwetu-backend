package com.kulakwetu.reference.repository;

import com.kulakwetu.reference.entity.AccountCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountCategoryRepository extends JpaRepository<AccountCategory, UUID> {
}
