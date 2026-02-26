package com.kulakwetu.agricash.controller;

import com.kulakwetu.agricash.dto.WithdrawalDecisionRequest;
import com.kulakwetu.agricash.dto.WithdrawalResponse;
import com.kulakwetu.agricash.service.WithdrawalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agricash/admin/withdrawals")
@RequiredArgsConstructor
@Tag(name = "AGRICASH - ADMIN")
@PreAuthorize("hasAnyRole('SYSADMIN','ADMIN')")
public class AgricashAdminWithdrawalController {

    private final WithdrawalService withdrawalService;

    @GetMapping
    public List<WithdrawalResponse> pending() { return withdrawalService.pending(); }

    @PostMapping("/{id}/approve")
    public WithdrawalResponse approve(@PathVariable UUID id, @Valid @RequestBody WithdrawalDecisionRequest req) {
        return withdrawalService.approve(id, req.idempotencyKey());
    }

    @PostMapping("/{id}/reject")
    public WithdrawalResponse reject(@PathVariable UUID id, @Valid @RequestBody WithdrawalDecisionRequest req) {
        return withdrawalService.reject(id, req.rejectionReason(), req.idempotencyKey());
    }
}
