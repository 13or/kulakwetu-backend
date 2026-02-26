package com.kulakwetu.agricash.controller;

import com.kulakwetu.agricash.dto.WithdrawalCreateRequest;
import com.kulakwetu.agricash.dto.WithdrawalResponse;
import com.kulakwetu.agricash.service.WithdrawalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agricash/withdrawals")
@RequiredArgsConstructor
@Tag(name = "AGRICASH - WITHDRAWALS")
@PreAuthorize("hasAnyRole('SUPPLIER')")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    @PostMapping
    public WithdrawalResponse request(@Valid @RequestBody WithdrawalCreateRequest request) {
        return withdrawalService.request(request);
    }

    @GetMapping
    public List<WithdrawalResponse> mine() {
        return withdrawalService.myRequests();
    }
}
