package com.kulakwetu.agrisol.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class DeliveryFeeService {

    public BigDecimal compute(UUID countryId, UUID provinceId, UUID cityId, UUID municipalityId) {
        return BigDecimal.valueOf(5);
    }
}
