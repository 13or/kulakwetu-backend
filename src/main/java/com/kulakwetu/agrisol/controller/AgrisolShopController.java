package com.kulakwetu.agrisol.controller;

import com.kulakwetu.agrisol.dto.ShopProductResponse;
import com.kulakwetu.agrisol.service.AgrisolCatalogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/agrisol/shop")
@RequiredArgsConstructor
@Tag(name = "AGRISOL - SHOP")
public class AgrisolShopController {

    private final AgrisolCatalogService service;

    @GetMapping("/products")
    public List<ShopProductResponse> products() { return service.shopProducts(); }
}
