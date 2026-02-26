package com.kulakwetu.identity.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class MfaService {
    private static final Base64.Encoder B32_FALLBACK = Base64.getEncoder();

    public String generateSecret() {
        byte[] bytes = new byte[20];
        new SecureRandom().nextBytes(bytes);
        return B32_FALLBACK.encodeToString(bytes);
    }

    public String generateQrBase64(String issuer, String accountName, String secret) {
        String otpAuthUri = "otpauth://totp/" + issuer + ":" + accountName + "?secret=" + secret + "&issuer=" + issuer;
        return Base64.getEncoder().encodeToString(otpAuthUri.getBytes(StandardCharsets.UTF_8));
    }

    public boolean verifyCode(String secret, String code) {
        long timestep = Instant.now().getEpochSecond() / 30;
        return code.equals(generateTotp(secret, timestep))
                || code.equals(generateTotp(secret, timestep - 1))
                || code.equals(generateTotp(secret, timestep + 1));
    }

    private String generateTotp(String secret, long timestep) {
        try {
            byte[] key = Base64.getDecoder().decode(secret);
            byte[] data = ByteBuffer.allocate(8).putLong(timestep).array();
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] hash = mac.doFinal(data);
            int offset = hash[hash.length - 1] & 0xF;
            int binary = ((hash[offset] & 0x7F) << 24)
                    | ((hash[offset + 1] & 0xFF) << 16)
                    | ((hash[offset + 2] & 0xFF) << 8)
                    | (hash[offset + 3] & 0xFF);
            int otp = binary % 1_000_000;
            return String.format("%06d", otp);
        } catch (Exception e) {
            return "";
        }
    }
}
