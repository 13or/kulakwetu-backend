package com.kulakwetu.agrisol.dto;

import com.kulakwetu.agrisol.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CheckoutRequest(
        @NotNull PaymentMethod paymentMethod,
        @NotBlank String currencyCode,
        @NotNull UUID countryId,
        @NotNull UUID provinceId,
        @NotNull UUID cityId,
        @NotNull UUID municipalityId,
        @NotBlank String addressLine,
        @NotBlank String contactName,
        @NotBlank String contactPhone,
        String notes
) {}
