package com.kulakwetu.reference.controller;

import com.kulakwetu.reference.dto.*;
import com.kulakwetu.reference.service.ReferenceDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/reference")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SYSADMIN','ADMIN')")
public class ReferenceDataAdminController {

    private final ReferenceDataService service;

    @Tag(name = "REFERENCE - ACCOUNT")
    @PostMapping("/account-types") @ResponseStatus(HttpStatus.CREATED)
    public AccountTypeResponse createAccountType(@Valid @RequestBody AccountTypeRequest request){ return service.createAccountType(request); }
    @Tag(name = "REFERENCE - ACCOUNT")
    @PutMapping("/account-types/{id}")
    public AccountTypeResponse updateAccountType(@PathVariable UUID id, @Valid @RequestBody AccountTypeRequest request){ return service.updateAccountType(id, request); }
    @Tag(name = "REFERENCE - ACCOUNT")
    @GetMapping("/account-types/{id}")
    public AccountTypeResponse getAccountType(@PathVariable UUID id){ return service.getAccountType(id); }
    @Tag(name = "REFERENCE - ACCOUNT")
    @GetMapping("/account-types")
    public Page<AccountTypeResponse> listAccountTypes(Pageable pageable){ return service.listAccountTypes(pageable); }
    @Tag(name = "REFERENCE - ACCOUNT")
    @DeleteMapping("/account-types/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountType(@PathVariable UUID id){ service.deleteAccountType(id); }

    @Tag(name = "REFERENCE - ACCOUNT")
    @PostMapping("/account-categories") @ResponseStatus(HttpStatus.CREATED)
    public AccountCategoryResponse createAccountCategory(@Valid @RequestBody AccountCategoryRequest request){ return service.createAccountCategory(request); }
    @Tag(name = "REFERENCE - ACCOUNT")
    @PutMapping("/account-categories/{id}")
    public AccountCategoryResponse updateAccountCategory(@PathVariable UUID id, @Valid @RequestBody AccountCategoryRequest request){ return service.updateAccountCategory(id, request); }
    @Tag(name = "REFERENCE - ACCOUNT")
    @GetMapping("/account-categories/{id}")
    public AccountCategoryResponse getAccountCategory(@PathVariable UUID id){ return service.getAccountCategory(id); }
    @Tag(name = "REFERENCE - ACCOUNT")
    @GetMapping("/account-categories")
    public Page<AccountCategoryResponse> listAccountCategories(Pageable pageable){ return service.listAccountCategories(pageable); }
    @Tag(name = "REFERENCE - ACCOUNT")
    @DeleteMapping("/account-categories/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountCategory(@PathVariable UUID id){ service.deleteAccountCategory(id); }

    @Tag(name = "REFERENCE - CURRENCY")
    @PostMapping("/currencies") @ResponseStatus(HttpStatus.CREATED)
    public CurrencyResponse createCurrency(@Valid @RequestBody CurrencyRequest request){ return service.createCurrency(request); }
    @Tag(name = "REFERENCE - CURRENCY")
    @PutMapping("/currencies/{id}")
    public CurrencyResponse updateCurrency(@PathVariable UUID id, @Valid @RequestBody CurrencyRequest request){ return service.updateCurrency(id, request); }
    @Tag(name = "REFERENCE - CURRENCY")
    @GetMapping("/currencies/{id}")
    public CurrencyResponse getCurrency(@PathVariable UUID id){ return service.getCurrency(id); }
    @Tag(name = "REFERENCE - CURRENCY")
    @GetMapping("/currencies")
    public Page<CurrencyResponse> listCurrencies(Pageable pageable){ return service.listCurrencies(pageable); }
    @Tag(name = "REFERENCE - CURRENCY")
    @DeleteMapping("/currencies/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrency(@PathVariable UUID id){ service.deleteCurrency(id); }

    @Tag(name = "REFERENCE - CURRENCY")
    @PostMapping("/currency-rates") @ResponseStatus(HttpStatus.CREATED)
    public CurrencyRateResponse createCurrencyRate(@Valid @RequestBody CurrencyRateRequest request){ return service.createCurrencyRate(request); }
    @Tag(name = "REFERENCE - CURRENCY")
    @PutMapping("/currency-rates/{id}")
    public CurrencyRateResponse updateCurrencyRate(@PathVariable UUID id, @Valid @RequestBody CurrencyRateRequest request){ return service.updateCurrencyRate(id, request); }
    @Tag(name = "REFERENCE - CURRENCY")
    @GetMapping("/currency-rates/{id}")
    public CurrencyRateResponse getCurrencyRate(@PathVariable UUID id){ return service.getCurrencyRate(id); }
    @Tag(name = "REFERENCE - CURRENCY")
    @GetMapping("/currency-rates")
    public Page<CurrencyRateResponse> listCurrencyRates(Pageable pageable){ return service.listCurrencyRates(pageable); }
    @Tag(name = "REFERENCE - CURRENCY")
    @DeleteMapping("/currency-rates/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrencyRate(@PathVariable UUID id){ service.deleteCurrencyRate(id); }

    @Tag(name = "REFERENCE - MEASURE")
    @PostMapping("/measure-unit-products") @ResponseStatus(HttpStatus.CREATED)
    public MeasureUnitProductResponse createMeasureUnitProduct(@Valid @RequestBody MeasureUnitProductRequest request){ return service.createMeasureUnitProduct(request); }
    @Tag(name = "REFERENCE - MEASURE")
    @PutMapping("/measure-unit-products/{id}")
    public MeasureUnitProductResponse updateMeasureUnitProduct(@PathVariable UUID id, @Valid @RequestBody MeasureUnitProductRequest request){ return service.updateMeasureUnitProduct(id, request); }
    @Tag(name = "REFERENCE - MEASURE")
    @GetMapping("/measure-unit-products/{id}")
    public MeasureUnitProductResponse getMeasureUnitProduct(@PathVariable UUID id){ return service.getMeasureUnitProduct(id); }
    @Tag(name = "REFERENCE - MEASURE")
    @GetMapping("/measure-unit-products")
    public Page<MeasureUnitProductResponse> listMeasureUnitProducts(Pageable pageable){ return service.listMeasureUnitProducts(pageable); }
    @Tag(name = "REFERENCE - MEASURE")
    @DeleteMapping("/measure-unit-products/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeasureUnitProduct(@PathVariable UUID id){ service.deleteMeasureUnitProduct(id); }

    @Tag(name = "REFERENCE - MEASURE")
    @PostMapping("/measure-unit-lands") @ResponseStatus(HttpStatus.CREATED)
    public MeasureUnitLandResponse createMeasureUnitLand(@Valid @RequestBody MeasureUnitLandRequest request){ return service.createMeasureUnitLand(request); }
    @Tag(name = "REFERENCE - MEASURE")
    @PutMapping("/measure-unit-lands/{id}")
    public MeasureUnitLandResponse updateMeasureUnitLand(@PathVariable UUID id, @Valid @RequestBody MeasureUnitLandRequest request){ return service.updateMeasureUnitLand(id, request); }
    @Tag(name = "REFERENCE - MEASURE")
    @GetMapping("/measure-unit-lands/{id}")
    public MeasureUnitLandResponse getMeasureUnitLand(@PathVariable UUID id){ return service.getMeasureUnitLand(id); }
    @Tag(name = "REFERENCE - MEASURE")
    @GetMapping("/measure-unit-lands")
    public Page<MeasureUnitLandResponse> listMeasureUnitLands(Pageable pageable){ return service.listMeasureUnitLands(pageable); }
    @Tag(name = "REFERENCE - MEASURE")
    @DeleteMapping("/measure-unit-lands/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeasureUnitLand(@PathVariable UUID id){ service.deleteMeasureUnitLand(id); }
}
