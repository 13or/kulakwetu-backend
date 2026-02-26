package com.kulakwetu.reference.repository;

import com.kulakwetu.reference.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountTypeRepository extends JpaRepository<AccountType, UUID> {
}
