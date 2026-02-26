package com.kulakwetu.agrisol.controller;

import com.kulakwetu.agrisol.dto.*;
import com.kulakwetu.agrisol.entity.SupplierBusiness;
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
@RequestMapping("/api/agrisol/supplier")
@RequiredArgsConstructor
@Tag(name = "AGRISOL - SUPPLIER")
@PreAuthorize("hasAnyRole('SUPPLIER')")
public class AgrisolSupplierController {

    private final AgrisolCatalogService service;

    @PutMapping("/business")
    public SupplierBusiness upsertBusiness(@Valid @RequestBody SupplierBusinessRequest request) { return service.upsertSupplierBusiness(request); }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public UserProductResponse createProduct(@Valid @RequestBody UserProductRequest request) { return service.createUserProduct(request); }

    @PutMapping("/products/{id}")
    public UserProductResponse updateProduct(@PathVariable UUID id, @Valid @RequestBody UserProductRequest request) { return service.updateUserProduct(id, request); }

    @PostMapping("/products/{id}/media")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addMedia(@PathVariable UUID id, @Valid @RequestBody UserProductMediaRequest request) { service.addMedia(id, request); }

    @PostMapping("/products/{id}/prices")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPrice(@PathVariable UUID id, @Valid @RequestBody UserProductPriceRequest request) { service.addPrice(id, request); }

    @GetMapping("/products")
    public List<UserProductResponse> myProducts() { return service.myProducts(); }
}
