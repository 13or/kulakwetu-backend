package com.kulakwetu.identity.gateway;

public interface SmsGateway {
    void sendVerificationCode(String phoneNumber, String code);

    void sendPasswordResetCode(String phoneNumber, String code);
}
