package com.kulakwetu.admin.controller;

import com.kulakwetu.admin.dto.SubscriptionTypeRequest;
import com.kulakwetu.admin.dto.SubscriptionTypeResponse;
import com.kulakwetu.admin.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/subscriptions")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SYSADMIN','ADMIN')")
@Tag(name = "ADMIN - SUBSCRIPTIONS")
public class AdminSubscriptionsController {

    private final AdminService adminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionTypeResponse createSubscription(@Valid @RequestBody SubscriptionTypeRequest request) {
        return adminService.createSubscription(request);
    }

    @PutMapping("/{id}")
    public SubscriptionTypeResponse updateSubscription(@PathVariable UUID id, @Valid @RequestBody SubscriptionTypeRequest request) {
        return adminService.updateSubscription(id, request);
    }

    @GetMapping("/{id}")
    public SubscriptionTypeResponse getSubscription(@PathVariable UUID id) {
        return adminService.getSubscription(id);
    }

    @GetMapping
    public Page<SubscriptionTypeResponse> listSubscriptions(Pageable pageable) {
        return adminService.listSubscriptions(pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(@PathVariable UUID id) {
        adminService.deleteSubscription(id);
    }
}
