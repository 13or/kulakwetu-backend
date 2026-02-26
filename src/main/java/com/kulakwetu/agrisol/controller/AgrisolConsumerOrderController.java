package com.kulakwetu.agrisol.controller;

import com.kulakwetu.agrisol.dto.CheckoutRequest;
import com.kulakwetu.agrisol.dto.OrderSummaryResponse;
import com.kulakwetu.agrisol.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agrisol/shop")
@RequiredArgsConstructor
@Tag(name = "AGRISOL - CONSUMER")
@PreAuthorize("hasAnyRole('CONSUMER')")
public class AgrisolConsumerOrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public OrderSummaryResponse checkout(@Valid @RequestBody CheckoutRequest request) { return orderService.checkout(request); }

    @GetMapping("/orders")
    public List<OrderSummaryResponse> myOrders() { return orderService.myOrders(); }

    @PostMapping("/orders/{id}/cancel")
    public void cancelOrder(@PathVariable UUID id) { orderService.cancelMyOrder(id); }
}
