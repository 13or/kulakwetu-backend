package com.kulakwetu.agrisol.controller;

import com.kulakwetu.agrisol.dto.AssignDriverRequest;
import com.kulakwetu.agrisol.dto.OrderSummaryResponse;
import com.kulakwetu.agrisol.service.AssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agrisol/manager/orders")
@RequiredArgsConstructor
@Tag(name = "AGRISOL - MANAGER")
@PreAuthorize("hasAnyRole('ORDER_MANAGER')")
public class AgrisolManagerController {

    private final AssignmentService assignmentService;

    @GetMapping
    public List<OrderSummaryResponse> orders() { return assignmentService.managerOrders(); }

    @PostMapping("/{id}/assign")
    public OrderSummaryResponse assign(@PathVariable UUID id, @Valid @RequestBody AssignDriverRequest request) {
        return assignmentService.assignDriver(id, request.driverUserId());
    }
}
