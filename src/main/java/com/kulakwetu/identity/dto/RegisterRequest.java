package com.kulakwetu.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotBlank @Pattern(regexp = "^\\+[1-9][0-9]{6,14}$", message = "Phone must include country code") String phoneNumber,
        @Email String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank @Pattern(regexp = "^(SUPPLIER|PRODUCER|CONSUMER)$", message = "accountType must be SUPPLIER, PRODUCER or CONSUMER") String accountType,
        @Pattern(regexp = "^(EMAIL|SMS)$", message = "verificationChannel must be EMAIL or SMS") String verificationChannel

) {
}
