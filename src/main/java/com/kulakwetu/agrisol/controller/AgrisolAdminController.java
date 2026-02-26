package com.kulakwetu.agrisol.controller;

import com.kulakwetu.agrisol.dto.*;
import com.kulakwetu.agrisol.entity.AgrisolProduct;
import com.kulakwetu.agrisol.entity.ProductCategory;
import com.kulakwetu.agrisol.service.AgrisolCatalogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agrisol/admin")
@RequiredArgsConstructor
@Tag(name = "AGRISOL - ADMIN")
@PreAuthorize("hasAnyRole('SYSADMIN','ADMIN')")
public class AgrisolAdminController {

    private final AgrisolCatalogService service;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCategory createCategory(@Valid @RequestBody ProductCategoryRequest request) { return service.createCategory(request); }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public AgrisolProduct createAgrisolProduct(@Valid @RequestBody AgrisolProductRequest request) { return service.createAgrisolProduct(request); }

    @GetMapping("/user-products/pending")
    public List<UserProductResponse> pendingUserProducts() { return service.pendingProducts(); }

    @PutMapping("/user-products/{id}/status")
    public UserProductResponse setStatus(@PathVariable UUID id, @Valid @RequestBody UserProductStatusRequest request) { return service.updateStatus(id, request); }
}
