package com.kulakwetu.admin.repository;

import com.kulakwetu.admin.entity.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, UUID> {
}
