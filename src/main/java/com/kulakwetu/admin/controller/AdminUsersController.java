package com.kulakwetu.admin.controller;

import com.kulakwetu.admin.dto.AdminUserResponse;
import com.kulakwetu.admin.dto.AdminUserStatusRequest;
import com.kulakwetu.admin.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SYSADMIN','ADMIN')")
@Tag(name = "ADMIN - USERS")
public class AdminUsersController {

    private final AdminService adminService;

    @GetMapping("/suppliers")
    public Page<AdminUserResponse> listSuppliers(Pageable pageable) {
        return adminService.listSuppliers(pageable);
    }

    @GetMapping("/consumers")
    public Page<AdminUserResponse> listConsumers(Pageable pageable) {
        return adminService.listConsumers(pageable);
    }

    @PatchMapping("/{id}/activation")
    public AdminUserResponse updateActivation(@PathVariable UUID id, @Valid @RequestBody AdminUserStatusRequest request) {
        return adminService.updateUserActivation(id, request.enabled());
    }
}
