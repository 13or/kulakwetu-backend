package com.kulakwetu.identity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";
    private static final String ISSUER = "kulakwetu-backend";
    private static final String AUDIENCE = "kulakwetu-api";

    @Test
    void createAccessToken_containsIssAudAndJti() {
        JwtService jwtService = new JwtService(SECRET, ISSUER, AUDIENCE, false);

        String token = jwtService.createAccessToken(UUID.randomUUID(), List.of("CONSUMER"), List.of("ORDER_ASSIGN"), 3600);
        Claims claims = jwtService.parse(token);

        assertEquals(ISSUER, claims.getIssuer());
        assertTrue(claims.getAudience().contains(AUDIENCE));
        assertNotNull(claims.getId());
        assertFalse(claims.getId().isBlank());
    }

    @Test
    void strictMode_rejectsMissingClaims() {
        JwtService strictService = new JwtService(SECRET, ISSUER, AUDIENCE, true);
        String legacyToken = buildLegacyTokenWithoutIssAudJti();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> strictService.parse(legacyToken));
        assertTrue(ex.getMessage().contains("Missing required JWT claim"));
    }

    @Test
    void strictMode_rejectsInvalidIssuerOrAudience() {
        JwtService strictService = new JwtService(SECRET, ISSUER, AUDIENCE, true);
        String invalidIssuerToken = buildTokenWithClaims("other-issuer", AUDIENCE, UUID.randomUUID().toString());
        String invalidAudienceToken = buildTokenWithClaims(ISSUER, "other-audience", UUID.randomUUID().toString());

        assertThrows(IllegalArgumentException.class, () -> strictService.parse(invalidIssuerToken));
        assertThrows(IllegalArgumentException.class, () -> strictService.parse(invalidAudienceToken));
    }

    @Test
    void compatibilityMode_acceptsLegacyTokenWithoutClaims() {
        JwtService compatService = new JwtService(SECRET, ISSUER, AUDIENCE, false);
        String legacyToken = buildLegacyTokenWithoutIssAudJti();

        Claims claims = compatService.parse(legacyToken);

        assertNotNull(claims);
        assertNotNull(claims.getSubject());
    }

    private String buildLegacyTokenWithoutIssAudJti() {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .claim("roles", List.of("CONSUMER"))
                .claim("permissions", List.of("ORDER_ASSIGN"))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600)))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    private String buildTokenWithClaims(String issuer, String audience, String jti) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .issuer(issuer)
                .audience().add(audience).and()
                .id(jti)
                .claim("roles", List.of("CONSUMER"))
                .claim("permissions", List.of("ORDER_ASSIGN"))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600)))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}