package com.kulakwetu.agricash.controller;

import com.kulakwetu.agricash.dto.*;
import com.kulakwetu.agricash.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/agricash/payments")
@RequiredArgsConstructor
@Tag(name = "AGRICASH - PAYMENTS")
@PreAuthorize("isAuthenticated()")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/intents")
    public PaymentIntentResponse createIntent(@Valid @RequestBody CreatePaymentIntentRequest request) {
        return paymentService.createIntent(request);
    }

    @PostMapping("/{id}/capture")
    public PaymentIntentResponse capture(@PathVariable UUID id, @Valid @RequestBody CapturePaymentRequest request) {
        return paymentService.capture(id, request);
    }

    @PostMapping("/{id}/refund")
    public PaymentIntentResponse refund(@PathVariable UUID id, @Valid @RequestBody RefundPaymentRequest request) {
        return paymentService.refund(id, request);
    }

    @GetMapping("/{id}")
    public PaymentIntentResponse get(@PathVariable UUID id) {
        return paymentService.get(id);
    }
}
