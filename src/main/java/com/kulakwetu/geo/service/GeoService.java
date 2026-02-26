package com.kulakwetu.geo.service;

import com.kulakwetu.common.exception.ResourceNotFoundException;
import com.kulakwetu.geo.dto.*;
import com.kulakwetu.geo.entity.*;
import com.kulakwetu.geo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GeoService {

    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final CityRepository cityRepository;
    private final MunicipalityRepository municipalityRepository;

    @Transactional
    public CountryResponse createCountry(CountryRequest request) {
        Country saved = countryRepository.save(Country.builder()
                .id(UUID.randomUUID())
                .name(request.name().trim())
                .isoCode(request.isoCode().trim())
                .phoneCode(request.phoneCode().trim())
                .isPublic(request.isPublic())
                .build());
        return toCountry(saved);
    }

    @Transactional
    public CountryResponse updateCountry(UUID id, CountryRequest request) {
        Country country = findCountry(id);
        country.setName(request.name().trim());
        country.setIsoCode(request.isoCode().trim());
        country.setPhoneCode(request.phoneCode().trim());
        country.setPublic(request.isPublic());
        return toCountry(countryRepository.save(country));
    }

    @Transactional(readOnly = true)
    public CountryResponse getCountry(UUID id) {
        return toCountry(findCountry(id));
    }

    @Transactional
    public void deleteCountry(UUID id) {
        countryRepository.delete(findCountry(id));
    }

    @Transactional(readOnly = true)
    public Page<CountryResponse> adminCountries(Pageable pageable) {
        return countryRepository.findAllByOrderByNameAsc(pageable).map(this::toCountry);
    }

    @Transactional
    public ProvinceResponse createProvince(ProvinceRequest request) {
        Province saved = provinceRepository.save(Province.builder()
                .id(UUID.randomUUID())
                .country(findCountry(request.countryId()))
                .name(request.name().trim())
                .isPublic(request.isPublic())
                .build());
        return toProvince(saved);
    }

    @Transactional
    public ProvinceResponse updateProvince(UUID id, ProvinceRequest request) {
        Province province = findProvince(id);
        province.setCountry(findCountry(request.countryId()));
        province.setName(request.name().trim());
        province.setPublic(request.isPublic());
        return toProvince(provinceRepository.save(province));
    }

    @Transactional(readOnly = true)
    public ProvinceResponse getProvince(UUID id) {
        return toProvince(findProvince(id));
    }

    @Transactional
    public void deleteProvince(UUID id) {
        provinceRepository.delete(findProvince(id));
    }

    @Transactional(readOnly = true)
    public Page<ProvinceResponse> adminProvinces(Pageable pageable) {
        return provinceRepository.findAllByOrderByNameAsc(pageable).map(this::toProvince);
    }

    @Transactional
    public CityResponse createCity(CityRequest request) {
        City saved = cityRepository.save(City.builder()
                .id(UUID.randomUUID())
                .province(findProvince(request.provinceId()))
                .name(request.name().trim())
                .isPublic(request.isPublic())
                .build());
        return toCity(saved);
    }

    @Transactional
    public CityResponse updateCity(UUID id, CityRequest request) {
        City city = findCity(id);
        city.setProvince(findProvince(request.provinceId()));
        city.setName(request.name().trim());
        city.setPublic(request.isPublic());
        return toCity(cityRepository.save(city));
    }

    @Transactional(readOnly = true)
    public CityResponse getCity(UUID id) {
        return toCity(findCity(id));
    }

    @Transactional
    public void deleteCity(UUID id) {
        cityRepository.delete(findCity(id));
    }

    @Transactional(readOnly = true)
    public Page<CityResponse> adminCities(Pageable pageable) {
        return cityRepository.findAllByOrderByNameAsc(pageable).map(this::toCity);
    }

    @Transactional
    public MunicipalityResponse createMunicipality(MunicipalityRequest request) {
        Municipality saved = municipalityRepository.save(Municipality.builder()
                .id(UUID.randomUUID())
                .city(findCity(request.cityId()))
                .name(request.name().trim())
                .isPublic(request.isPublic())
                .build());
        return toMunicipality(saved);
    }

    @Transactional
    public MunicipalityResponse updateMunicipality(UUID id, MunicipalityRequest request) {
        Municipality municipality = findMunicipality(id);
        municipality.setCity(findCity(request.cityId()));
        municipality.setName(request.name().trim());
        municipality.setPublic(request.isPublic());
        return toMunicipality(municipalityRepository.save(municipality));
    }

    @Transactional(readOnly = true)
    public MunicipalityResponse getMunicipality(UUID id) {
        return toMunicipality(findMunicipality(id));
    }

    @Transactional
    public void deleteMunicipality(UUID id) {
        municipalityRepository.delete(findMunicipality(id));
    }

    @Transactional(readOnly = true)
    public Page<MunicipalityResponse> adminMunicipalities(Pageable pageable) {
        return municipalityRepository.findAllByOrderByNameAsc(pageable).map(this::toMunicipality);
    }

    @Transactional(readOnly = true)
    public List<CountryResponse> publicCountries() {
        return countryRepository.findByIsPublicTrueOrderByNameAsc().stream().map(this::toCountry).toList();
    }

    @Transactional(readOnly = true)
    public List<ProvinceResponse> publicProvinces(UUID countryId) {
        return provinceRepository.findByIsPublicTrueAndCountry_IdOrderByNameAsc(countryId).stream().map(this::toProvince).toList();
    }

    @Transactional(readOnly = true)
    public List<CityResponse> publicCities(UUID provinceId) {
        return cityRepository.findByIsPublicTrueAndProvince_IdOrderByNameAsc(provinceId).stream().map(this::toCity).toList();
    }

    @Transactional(readOnly = true)
    public List<MunicipalityResponse> publicMunicipalities(UUID cityId) {
        return municipalityRepository.findByIsPublicTrueAndCity_IdOrderByNameAsc(cityId).stream().map(this::toMunicipality).toList();
    }

    private Country findCountry(UUID id) {
        return countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country not found"));
    }

    private Province findProvince(UUID id) {
        return provinceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Province not found"));
    }

    private City findCity(UUID id) {
        return cityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("City not found"));
    }

    private Municipality findMunicipality(UUID id) {
        return municipalityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Municipality not found"));
    }

    private CountryResponse toCountry(Country c) {
        return new CountryResponse(c.getId(), c.getName(), c.getIsoCode(), c.getPhoneCode(), c.isPublic());
    }

    private ProvinceResponse toProvince(Province p) {
        return new ProvinceResponse(p.getId(), p.getCountry().getId(), p.getName(), p.isPublic());
    }

    private CityResponse toCity(City c) {
        return new CityResponse(c.getId(), c.getProvince().getId(), c.getName(), c.isPublic());
    }

    private MunicipalityResponse toMunicipality(Municipality m) {
        return new MunicipalityResponse(m.getId(), m.getCity().getId(), m.getName(), m.isPublic());
    }
}
