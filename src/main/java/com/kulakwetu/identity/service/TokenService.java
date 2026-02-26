package com.kulakwetu.identity.service;

import com.kulakwetu.identity.entity.PasswordResetToken;
import com.kulakwetu.identity.entity.RefreshToken;
import com.kulakwetu.identity.entity.VerificationToken;
import com.kulakwetu.identity.enums.TokenChannel;
import com.kulakwetu.identity.repository.PasswordResetTokenRepository;
import com.kulakwetu.identity.repository.RefreshTokenRepository;
import com.kulakwetu.identity.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    public RefreshToken createRefreshToken(UUID userId, long ttlSeconds) {
        RefreshToken token = RefreshToken.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .token(generateHexToken())
                .revoked(false)
                .createdAt(OffsetDateTime.now())
                .expiresAt(OffsetDateTime.now().plusSeconds(ttlSeconds))
                .build();
        return refreshTokenRepository.save(token);
    }

    @Transactional
    public VerificationToken createVerificationToken(UUID userId, TokenChannel channel) {
        VerificationToken token = VerificationToken.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .channel(channel)
                .token(generateUniqueVerificationToken(channel))
                .consumed(false)
                .createdAt(OffsetDateTime.now())
                .expiresAt(OffsetDateTime.now().plusHours(24))
                .build();
        return verificationTokenRepository.save(token);
    }

    @Transactional
    public PasswordResetToken createPasswordResetToken(UUID userId, TokenChannel channel) {
        PasswordResetToken token = PasswordResetToken.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .token(generateUniquePasswordResetToken(channel))
                .consumed(false)
                .createdAt(OffsetDateTime.now())
                .expiresAt(OffsetDateTime.now().plusHours(1))
                .build();
        return passwordResetTokenRepository.save(token);
    }

    private String generateUniqueVerificationToken(TokenChannel channel) {
        String token;
        do {
            token = channel == TokenChannel.SMS ? generateSixDigitsCode() : generateHexToken();
        } while (verificationTokenRepository.existsByToken(token));
        return token;
    }

    private String generateUniquePasswordResetToken(TokenChannel channel) {
        String token;
        do {
            token = channel == TokenChannel.SMS ? generateSixDigitsCode() : generateHexToken();
        } while (passwordResetTokenRepository.existsByToken(token));
        return token;
    }

    private String generateSixDigitsCode() {
        int code = SECURE_RANDOM.nextInt(1_000_000);
        return String.format("%06d", code);
    }

    private String generateHexToken() {
        return HexFormat.of().formatHex(SECURE_RANDOM.generateSeed(32));
    }
}
