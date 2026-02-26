package com.kulakwetu.agrisol.controller;

import com.kulakwetu.agrisol.dto.CartItemRequest;
import com.kulakwetu.agrisol.dto.CartItemResponse;
import com.kulakwetu.agrisol.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agrisol/shop/cart")
@RequiredArgsConstructor
@Tag(name = "AGRISOL - CONSUMER")
@PreAuthorize("hasAnyRole('CONSUMER')")
public class AgrisolCartController {

    private final CartService cartService;

    @GetMapping
    public List<CartItemResponse> myCart() { return cartService.getMyCartItems(); }

    @PostMapping("/items")
    public List<CartItemResponse> addItem(@Valid @RequestBody CartItemRequest request) { return cartService.addOrUpdateItem(request); }

    @DeleteMapping("/items/{id}")
    public void remove(@PathVariable UUID id) { cartService.removeItem(id); }

    @DeleteMapping
    public void clear() { cartService.clearMyCart(); }
}
