package com.kulakwetu.identity.gateway;

public interface EmailGateway {
    void sendVerificationLink(String email, String verificationLink);

    void sendPasswordResetLink(String email, String resetLink);
}
