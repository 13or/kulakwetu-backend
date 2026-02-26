package com.kulakwetu.geo.controller;

import com.kulakwetu.geo.dto.*;
import com.kulakwetu.geo.service.GeoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/geo")
@RequiredArgsConstructor
@Tag(name = "GEO - PUBLIC")
public class GeoPublicController {

    private final GeoService geoService;

    @GetMapping("/countries")
    public List<CountryResponse> countries() {
        return geoService.publicCountries();
    }

    @GetMapping("/countries/{countryId}/provinces")
    public List<ProvinceResponse> provinces(@PathVariable UUID countryId) {
        return geoService.publicProvinces(countryId);
    }

    @GetMapping("/provinces/{provinceId}/cities")
    public List<CityResponse> cities(@PathVariable UUID provinceId) {
        return geoService.publicCities(provinceId);
    }

    @GetMapping("/cities/{cityId}/municipalities")
    public List<MunicipalityResponse> municipalities(@PathVariable UUID cityId) {
        return geoService.publicMunicipalities(cityId);
    }
}
