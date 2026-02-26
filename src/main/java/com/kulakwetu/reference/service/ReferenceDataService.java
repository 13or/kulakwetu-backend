package com.kulakwetu.reference.service;

import com.kulakwetu.common.exception.ResourceNotFoundException;
import com.kulakwetu.reference.dto.*;
import com.kulakwetu.reference.entity.*;
import com.kulakwetu.reference.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReferenceDataService {

    private final AccountTypeRepository accountTypeRepository;
    private final AccountCategoryRepository accountCategoryRepository;
    private final CurrencyRepository currencyRepository;
    private final CurrencyRateRepository currencyRateRepository;
    private final MeasureUnitProductRepository measureUnitProductRepository;
    private final MeasureUnitLandRepository measureUnitLandRepository;

    @Transactional
    public AccountTypeResponse createAccountType(AccountTypeRequest request) {
        var s = accountTypeRepository.save(AccountType.builder().id(UUID.randomUUID()).code(request.code()).name(request.name()).isActive(request.isActive()).build());
        return new AccountTypeResponse(s.getId(), s.getCode(), s.getName(), s.isActive());
    }
    @Transactional
    public AccountTypeResponse updateAccountType(UUID id, AccountTypeRequest request) {
        var e = accountTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("AccountType not found"));
        e.setCode(request.code()); e.setName(request.name()); e.setActive(request.isActive());
        var s = accountTypeRepository.save(e);
        return new AccountTypeResponse(s.getId(), s.getCode(), s.getName(), s.isActive());
    }
    @Transactional(readOnly = true)
    public AccountTypeResponse getAccountType(UUID id) {
        var e = accountTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("AccountType not found"));
        return new AccountTypeResponse(e.getId(), e.getCode(), e.getName(), e.isActive());
    }
    @Transactional
    public void deleteAccountType(UUID id){ accountTypeRepository.delete(accountTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("AccountType not found"))); }

    @Transactional
    public AccountCategoryResponse createAccountCategory(AccountCategoryRequest request) {
        var s = accountCategoryRepository.save(AccountCategory.builder().id(UUID.randomUUID()).code(request.code()).name(request.name()).isActive(request.isActive()).build());
        return new AccountCategoryResponse(s.getId(), s.getCode(), s.getName(), s.isActive());
    }
    @Transactional
    public AccountCategoryResponse updateAccountCategory(UUID id, AccountCategoryRequest request) {
        var e = accountCategoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("AccountCategory not found"));
        e.setCode(request.code()); e.setName(request.name()); e.setActive(request.isActive());
        var s = accountCategoryRepository.save(e);
        return new AccountCategoryResponse(s.getId(), s.getCode(), s.getName(), s.isActive());
    }
    @Transactional(readOnly = true)
    public AccountCategoryResponse getAccountCategory(UUID id) {
        var e = accountCategoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("AccountCategory not found"));
        return new AccountCategoryResponse(e.getId(), e.getCode(), e.getName(), e.isActive());
    }
    @Transactional
    public void deleteAccountCategory(UUID id){ accountCategoryRepository.delete(accountCategoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("AccountCategory not found"))); }

    @Transactional
    public CurrencyResponse createCurrency(CurrencyRequest request) {
        var s = currencyRepository.save(Currency.builder().id(UUID.randomUUID()).code(request.code()).name(request.name()).symbol(request.symbol()).decimals(request.decimals()).isActive(request.isActive()).build());
        return new CurrencyResponse(s.getId(), s.getCode(), s.getName(), s.getSymbol(), s.getDecimals(), s.isActive());
    }
    @Transactional
    public CurrencyResponse updateCurrency(UUID id, CurrencyRequest request) {
        var e = currencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency not found"));
        e.setCode(request.code()); e.setName(request.name()); e.setSymbol(request.symbol()); e.setDecimals(request.decimals()); e.setActive(request.isActive());
        var s = currencyRepository.save(e);
        return new CurrencyResponse(s.getId(), s.getCode(), s.getName(), s.getSymbol(), s.getDecimals(), s.isActive());
    }
    @Transactional(readOnly = true)
    public CurrencyResponse getCurrency(UUID id) {
        var e = currencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency not found"));
        return new CurrencyResponse(e.getId(), e.getCode(), e.getName(), e.getSymbol(), e.getDecimals(), e.isActive());
    }
    @Transactional
    public void deleteCurrency(UUID id){ currencyRepository.delete(currencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency not found"))); }

    @Transactional
    public CurrencyRateResponse createCurrencyRate(CurrencyRateRequest request) {
        var base = currencyRepository.findById(request.baseCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Base currency not found"));
        var quote = currencyRepository.findById(request.quoteCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Quote currency not found"));
        var s = currencyRateRepository.save(CurrencyRate.builder().id(UUID.randomUUID()).baseCurrency(base).quoteCurrency(quote).rate(request.rate()).validFrom(request.validFrom()).build());
        return new CurrencyRateResponse(s.getId(), s.getBaseCurrency().getId(), s.getQuoteCurrency().getId(), s.getRate(), s.getValidFrom());
    }
    @Transactional
    public CurrencyRateResponse updateCurrencyRate(UUID id, CurrencyRateRequest request) {
        var e = currencyRateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CurrencyRate not found"));
        var base = currencyRepository.findById(request.baseCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Base currency not found"));
        var quote = currencyRepository.findById(request.quoteCurrencyId()).orElseThrow(() -> new ResourceNotFoundException("Quote currency not found"));
        e.setBaseCurrency(base); e.setQuoteCurrency(quote); e.setRate(request.rate()); e.setValidFrom(request.validFrom());
        var s = currencyRateRepository.save(e);
        return new CurrencyRateResponse(s.getId(), s.getBaseCurrency().getId(), s.getQuoteCurrency().getId(), s.getRate(), s.getValidFrom());
    }
    @Transactional(readOnly = true)
    public CurrencyRateResponse getCurrencyRate(UUID id) {
        var e = currencyRateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CurrencyRate not found"));
        return new CurrencyRateResponse(e.getId(), e.getBaseCurrency().getId(), e.getQuoteCurrency().getId(), e.getRate(), e.getValidFrom());
    }
    @Transactional
    public void deleteCurrencyRate(UUID id){ currencyRateRepository.delete(currencyRateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CurrencyRate not found"))); }

    @Transactional
    public MeasureUnitProductResponse createMeasureUnitProduct(MeasureUnitProductRequest request) {
        var s = measureUnitProductRepository.save(MeasureUnitProduct.builder().id(UUID.randomUUID()).name(request.name()).symbol(request.symbol()).isPublic(request.isPublic()).build());
        return new MeasureUnitProductResponse(s.getId(), s.getName(), s.getSymbol(), s.isPublic());
    }
    @Transactional
    public MeasureUnitProductResponse updateMeasureUnitProduct(UUID id, MeasureUnitProductRequest request) {
        var e = measureUnitProductRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MeasureUnitProduct not found"));
        e.setName(request.name()); e.setSymbol(request.symbol()); e.setPublic(request.isPublic());
        var s = measureUnitProductRepository.save(e);
        return new MeasureUnitProductResponse(s.getId(), s.getName(), s.getSymbol(), s.isPublic());
    }
    @Transactional(readOnly = true)
    public MeasureUnitProductResponse getMeasureUnitProduct(UUID id) {
        var e = measureUnitProductRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MeasureUnitProduct not found"));
        return new MeasureUnitProductResponse(e.getId(), e.getName(), e.getSymbol(), e.isPublic());
    }
    @Transactional
    public void deleteMeasureUnitProduct(UUID id){ measureUnitProductRepository.delete(measureUnitProductRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MeasureUnitProduct not found"))); }

    @Transactional
    public MeasureUnitLandResponse createMeasureUnitLand(MeasureUnitLandRequest request) {
        var s = measureUnitLandRepository.save(MeasureUnitLand.builder().id(UUID.randomUUID()).name(request.name()).symbol(request.symbol()).isPublic(request.isPublic()).build());
        return new MeasureUnitLandResponse(s.getId(), s.getName(), s.getSymbol(), s.isPublic());
    }
    @Transactional
    public MeasureUnitLandResponse updateMeasureUnitLand(UUID id, MeasureUnitLandRequest request) {
        var e = measureUnitLandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MeasureUnitLand not found"));
        e.setName(request.name()); e.setSymbol(request.symbol()); e.setPublic(request.isPublic());
        var s = measureUnitLandRepository.save(e);
        return new MeasureUnitLandResponse(s.getId(), s.getName(), s.getSymbol(), s.isPublic());
    }
    @Transactional(readOnly = true)
    public MeasureUnitLandResponse getMeasureUnitLand(UUID id) {
        var e = measureUnitLandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MeasureUnitLand not found"));
        return new MeasureUnitLandResponse(e.getId(), e.getName(), e.getSymbol(), e.isPublic());
    }
    @Transactional
    public void deleteMeasureUnitLand(UUID id){ measureUnitLandRepository.delete(measureUnitLandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MeasureUnitLand not found"))); }

    @Transactional(readOnly = true)
    public Page<AccountTypeResponse> listAccountTypes(Pageable pageable){
        return accountTypeRepository.findAll(pageable).map(e -> new AccountTypeResponse(e.getId(), e.getCode(), e.getName(), e.isActive()));
    }
    @Transactional(readOnly = true)
    public Page<AccountCategoryResponse> listAccountCategories(Pageable pageable){
        return accountCategoryRepository.findAll(pageable).map(e -> new AccountCategoryResponse(e.getId(), e.getCode(), e.getName(), e.isActive()));
    }
    @Transactional(readOnly = true)
    public Page<CurrencyResponse> listCurrencies(Pageable pageable){
        return currencyRepository.findAll(pageable).map(e -> new CurrencyResponse(e.getId(), e.getCode(), e.getName(), e.getSymbol(), e.getDecimals(), e.isActive()));
    }
    @Transactional(readOnly = true)
    public Page<CurrencyRateResponse> listCurrencyRates(Pageable pageable){
        return currencyRateRepository.findAll(pageable).map(e -> new CurrencyRateResponse(e.getId(), e.getBaseCurrency().getId(), e.getQuoteCurrency().getId(), e.getRate(), e.getValidFrom()));
    }
    @Transactional(readOnly = true)
    public Page<MeasureUnitProductResponse> listMeasureUnitProducts(Pageable pageable){
        return measureUnitProductRepository.findAll(pageable).map(e -> new MeasureUnitProductResponse(e.getId(), e.getName(), e.getSymbol(), e.isPublic()));
    }
    @Transactional(readOnly = true)
    public Page<MeasureUnitLandResponse> listMeasureUnitLands(Pageable pageable){
        return measureUnitLandRepository.findAll(pageable).map(e -> new MeasureUnitLandResponse(e.getId(), e.getName(), e.getSymbol(), e.isPublic()));
    }

}
