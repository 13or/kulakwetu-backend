package com.kulakwetu.admin.controller;

import com.kulakwetu.admin.dto.CompanyInfoRequest;
import com.kulakwetu.admin.dto.CompanyInfoResponse;
import com.kulakwetu.admin.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/company")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SYSADMIN','ADMIN')")
@Tag(name = "ADMIN - COMPANY")
public class AdminCompanyController {

    private final AdminService adminService;

    @GetMapping
    public CompanyInfoResponse getCompanyInfo() {
        return adminService.getCompanyInfo();
    }

    @PutMapping
    public CompanyInfoResponse upsertCompanyInfo(@Valid @RequestBody CompanyInfoRequest request) {
        return adminService.upsertCompanyInfo(request);
    }
}
