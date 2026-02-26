package com.kulakwetu.geo.controller;

import com.kulakwetu.geo.dto.*;
import com.kulakwetu.geo.service.GeoService;
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
@RequestMapping("/api/admin/geo")
@RequiredArgsConstructor
@Tag(name = "GEO - ADMIN")
@PreAuthorize("hasAnyRole('SYSADMIN','ADMIN')")
public class GeoAdminController {

    private final GeoService geoService;

    @PostMapping("/countries")
    @ResponseStatus(HttpStatus.CREATED)
    public CountryResponse createCountry(@Valid @RequestBody CountryRequest request) { return geoService.createCountry(request); }

    @PutMapping("/countries/{id}")
    public CountryResponse updateCountry(@PathVariable UUID id, @Valid @RequestBody CountryRequest request) { return geoService.updateCountry(id, request); }

    @GetMapping("/countries/{id}")
    public CountryResponse getCountry(@PathVariable UUID id) { return geoService.getCountry(id); }

    @DeleteMapping("/countries/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCountry(@PathVariable UUID id) { geoService.deleteCountry(id); }

    @GetMapping("/countries")
    public Page<CountryResponse> listCountries(Pageable pageable) { return geoService.adminCountries(pageable); }

    @PostMapping("/provinces")
    @ResponseStatus(HttpStatus.CREATED)
    public ProvinceResponse createProvince(@Valid @RequestBody ProvinceRequest request) { return geoService.createProvince(request); }

    @PutMapping("/provinces/{id}")
    public ProvinceResponse updateProvince(@PathVariable UUID id, @Valid @RequestBody ProvinceRequest request) { return geoService.updateProvince(id, request); }

    @GetMapping("/provinces/{id}")
    public ProvinceResponse getProvince(@PathVariable UUID id) { return geoService.getProvince(id); }

    @DeleteMapping("/provinces/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProvince(@PathVariable UUID id) { geoService.deleteProvince(id); }

    @GetMapping("/provinces")
    public Page<ProvinceResponse> listProvinces(Pageable pageable) { return geoService.adminProvinces(pageable); }

    @PostMapping("/cities")
    @ResponseStatus(HttpStatus.CREATED)
    public CityResponse createCity(@Valid @RequestBody CityRequest request) { return geoService.createCity(request); }

    @PutMapping("/cities/{id}")
    public CityResponse updateCity(@PathVariable UUID id, @Valid @RequestBody CityRequest request) { return geoService.updateCity(id, request); }

    @GetMapping("/cities/{id}")
    public CityResponse getCity(@PathVariable UUID id) { return geoService.getCity(id); }

    @DeleteMapping("/cities/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCity(@PathVariable UUID id) { geoService.deleteCity(id); }

    @GetMapping("/cities")
    public Page<CityResponse> listCities(Pageable pageable) { return geoService.adminCities(pageable); }

    @PostMapping("/municipalities")
    @ResponseStatus(HttpStatus.CREATED)
    public MunicipalityResponse createMunicipality(@Valid @RequestBody MunicipalityRequest request) { return geoService.createMunicipality(request); }

    @PutMapping("/municipalities/{id}")
    public MunicipalityResponse updateMunicipality(@PathVariable UUID id, @Valid @RequestBody MunicipalityRequest request) { return geoService.updateMunicipality(id, request); }

    @GetMapping("/municipalities/{id}")
    public MunicipalityResponse getMunicipality(@PathVariable UUID id) { return geoService.getMunicipality(id); }

    @DeleteMapping("/municipalities/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMunicipality(@PathVariable UUID id) { geoService.deleteMunicipality(id); }

    @GetMapping("/municipalities")
    public Page<MunicipalityResponse> listMunicipalities(Pageable pageable) { return geoService.adminMunicipalities(pageable); }
}
