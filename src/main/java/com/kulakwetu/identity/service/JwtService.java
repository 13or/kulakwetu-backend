package com.kulakwetu.identity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class JwtService {
    private final SecretKey key;
    private final String issuer;
    private final String audience;
    private final boolean strictClaimValidation;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.issuer:kulakwetu-backend}") String issuer,
            @Value("${app.jwt.audience:kulakwetu-api}") String audience,
            @Value("${app.jwt.strict-claim-validation:false}") boolean strictClaimValidation
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.audience = audience;
        this.strictClaimValidation = strictClaimValidation;
    }

    public String createAccessToken(UUID userId, List<String> roles, List<String> permissions, long ttlSeconds) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .issuer(issuer)
                .audience().add(audience).and()
                .id(UUID.randomUUID().toString())
                .claim("roles", roles)
                .claim("permissions", permissions)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(key)
                .compact();
    }

    public Claims parse(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

        validateIssuer(claims);
        validateAudience(claims);
        validateJti(claims);

        return claims;
    }

    private void validateIssuer(Claims claims) {
        String tokenIssuer = claims.getIssuer();
        if (tokenIssuer == null || tokenIssuer.isBlank()) {
            handleMissingClaim("iss");
            return;
        }
        if (!issuer.equals(tokenIssuer)) {
            throw new IllegalArgumentException("Invalid JWT issuer");
        }
    }

    private void validateAudience(Claims claims) {
        var audiences = claims.getAudience();
        if (audiences == null || audiences.isEmpty()) {
            handleMissingClaim("aud");
            return;
        }
        if (!audiences.contains(audience)) {
            throw new IllegalArgumentException("Invalid JWT audience");
        }
    }

    private void validateJti(Claims claims) {
        String jti = claims.getId();
        if (jti == null || jti.isBlank()) {
            handleMissingClaim("jti");
        }
    }

    private void handleMissingClaim(String claim) {
        if (strictClaimValidation) {
            throw new IllegalArgumentException("Missing required JWT claim: " + claim);
        }
        log.warn("JWT missing '{}' claim - accepted in compatibility mode", claim);
    }
}