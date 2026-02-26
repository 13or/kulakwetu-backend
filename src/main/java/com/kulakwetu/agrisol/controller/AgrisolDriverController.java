package com.kulakwetu.agrisol.controller;

import com.kulakwetu.agrisol.dto.OrderSummaryResponse;
import com.kulakwetu.agrisol.service.AssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agrisol/driver/orders")
@RequiredArgsConstructor
@Tag(name = "AGRISOL - DRIVER")
@PreAuthorize("hasAnyRole('DELIVERY_DRIVER')")
public class AgrisolDriverController {

    private final AssignmentService assignmentService;

    @GetMapping
    public List<OrderSummaryResponse> myOrders() { return assignmentService.myDriverOrders(); }

    @PostMapping("/{id}/delivered")
    public OrderSummaryResponse delivered(@PathVariable UUID id) { return assignmentService.markDelivered(id); }
}
