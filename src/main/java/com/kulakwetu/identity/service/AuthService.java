package com.kulakwetu.identity.service;

import com.kulakwetu.agricash.entity.Wallet;
import com.kulakwetu.agricash.enums.WalletStatus;
import com.kulakwetu.agricash.repository.WalletRepository;
import com.kulakwetu.common.exception.DomainException;
import com.kulakwetu.common.exception.ResourceNotFoundException;
import com.kulakwetu.common.util.IdentifierUtils;
import com.kulakwetu.identity.dto.*;
import lombok.extern.slf4j.Slf4j;
import com.kulakwetu.identity.entity.*;
import com.kulakwetu.identity.enums.TokenChannel;
import com.kulakwetu.identity.gateway.EmailGateway;
import com.kulakwetu.identity.gateway.SmsGateway;
import com.kulakwetu.identity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final MfaService mfaService;
    private final SmsGateway smsGateway;
    private final EmailGateway emailGateway;

    @Value("${app.jwt.access-token-ttl-seconds:3600}")
    private long accessTtl;

    @Value("${app.jwt.refresh-token-ttl-seconds:604800}")
    private long refreshTtl;

    @Value("${app.jwt.refresh-token-remember-ttl-seconds:2592000}")
    private long rememberRefreshTtl;

    @Value("${app.identity.verification-link-base-url:http://localhost:4200/auth/verify?token=}")
    private String verificationLinkBaseUrl;

    @Value("${app.identity.reset-password-link-base-url:http://localhost:4200/auth/reset-password?token=}")
    private String resetPasswordLinkBaseUrl;

    @Value("${app.agricash.default-currency:CDF}")
    private String defaultWalletCurrency;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        validateUniqueness(request);

        User user = userRepository.save(User.builder()
                .id(UUID.randomUUID())
                .username(request.username().trim())
                .phoneNumber(request.phoneNumber().trim())
                .email(request.email() == null || request.email().isBlank() ? null : request.email().trim().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.password()))
                .firstName(request.firstName().trim())
                .lastName(request.lastName().trim())
                .accountType(request.accountType())
                .enabled(true)
                .verified(false)
                .mfaEnabled(false)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build());

        Role role = resolveDefaultRole(request.accountType());
        userRoleRepository.save(UserRole.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .roleId(role.getId())
                .createdAt(OffsetDateTime.now())
                .build());

        Wallet wallet = walletRepository.save(Wallet.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .currency(defaultWalletCurrency)
                .balance(BigDecimal.ZERO)
                .status(WalletStatus.ACTIVE)
                .createdAt(OffsetDateTime.now())
                .build());

        VerificationToken verificationToken = tokenService.createVerificationToken(
                user.getId(), resolveVerificationChannel(request));
        dispatchVerification(user, verificationToken);

        return new RegisterResponse(user.getId(), wallet.getId(), verificationToken.getToken());
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = resolveByIdentifier(request.identifier()).orElseThrow(() -> new DomainException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new DomainException("Invalid credentials");
        }
        if (!user.isEnabled()) {
            throw new DomainException("Account disabled");
        }

        if (user.isMfaEnabled()) {
            return new LoginResponse(null, 0, null, 0, true);
        }

        return issueTokens(user, request.rememberMe());
    }

    @Transactional
    public LoginResponse refresh(RefreshRequest request) {
        RefreshToken token = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new DomainException("Invalid refresh token"));

        if (token.isRevoked() || token.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new DomainException("Refresh token expired or revoked");
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        token.setRevoked(true);
        refreshTokenRepository.save(token);

        return issueTokens(user, false);
    }

    @Transactional
    public void verifyAccount(VerifyAccountRequest request) {
        VerificationToken token = verificationTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new DomainException("Invalid verification token"));
        if (token.isConsumed() || token.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new DomainException("Verification token expired or consumed");
        }
        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setVerified(true);
        user.setUpdatedAt(OffsetDateTime.now());
        token.setConsumed(true);
        userRepository.save(user);
        verificationTokenRepository.save(token);
    }

    @Transactional
    public String forgotPassword(ForgotPasswordRequest request) {
        resolveByIdentifier(request.identifier())
                .ifPresent(this::dispatchPasswordReset);
        return "If account exists, reset instructions were sent";
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new DomainException("Invalid reset token"));

        if (token.isConsumed() || token.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new DomainException("Reset token expired or consumed");
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        user.setUpdatedAt(OffsetDateTime.now());
        token.setConsumed(true);
        userRepository.save(user);
        passwordResetTokenRepository.save(token);
    }

    @Transactional
    public MfaSetupResponse beginMfaSetup() {
        User user = currentUser();
        String secret = mfaService.generateSecret();
        user.setMfaSecret(secret);
        user.setUpdatedAt(OffsetDateTime.now());
        userRepository.save(user);

        String accountName = user.getEmail() != null ? user.getEmail() : user.getPhoneNumber();
        String qr = mfaService.generateQrBase64("KULAKWETU", accountName, secret);
        return new MfaSetupResponse(secret, qr);
    }

    @Transactional
    public void confirmMfa(MfaConfirmRequest request) {
        User user = currentUser();
        if (user.getMfaSecret() == null || user.getMfaSecret().isBlank()) {
            throw new DomainException("MFA setup not started");
        }
        if (!mfaService.verifyCode(user.getMfaSecret(), request.code())) {
            throw new DomainException("Invalid MFA code");
        }
        user.setMfaEnabled(true);
        user.setUpdatedAt(OffsetDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public LoginResponse verifyMfa(MfaVerifyRequest request) {
        User user = resolveByIdentifier(request.identifier())
                .orElseThrow(() -> new DomainException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new DomainException("Invalid credentials");
        }
        if (!user.isMfaEnabled() || user.getMfaSecret() == null) {
            throw new DomainException("MFA not enabled");
        }
        if (!mfaService.verifyCode(user.getMfaSecret(), request.code())) {
            throw new DomainException("Invalid MFA code");
        }
        return issueTokens(user, request.rememberMe());
    }



    private void dispatchPasswordReset(User user) {
        TokenChannel channel = (user.getEmail() != null && !user.getEmail().isBlank()) ? TokenChannel.EMAIL : TokenChannel.SMS;
        PasswordResetToken resetToken = tokenService.createPasswordResetToken(user.getId(), channel);

        try {
            if (channel == TokenChannel.EMAIL) {
                String link = resetPasswordLinkBaseUrl + resetToken.getToken();
                emailGateway.sendPasswordResetLink(user.getEmail(), link);
                return;
            }
            smsGateway.sendPasswordResetCode(user.getPhoneNumber(), resetToken.getToken());
        } catch (RuntimeException ex) {
            log.error("Failed to dispatch password reset token for user {} via {}", user.getId(), channel, ex);
        }
    }

    private void dispatchVerification(User user, VerificationToken verificationToken) {
        try {
            if (verificationToken.getChannel() == TokenChannel.SMS) {
                smsGateway.sendVerificationCode(user.getPhoneNumber(), verificationToken.getToken());
                return;
            }

            String link = verificationLinkBaseUrl + verificationToken.getToken();
            emailGateway.sendVerificationLink(user.getEmail(), link);
        } catch (RuntimeException ex) {
            log.error("Failed to dispatch verification token for user {} via {}", user.getId(), verificationToken.getChannel(), ex);
        }
    }


    private TokenChannel resolveVerificationChannel(RegisterRequest request) {
        String requestedChannel = request.verificationChannel();
        if (requestedChannel == null || requestedChannel.isBlank()) {
            return request.email() != null && !request.email().isBlank() ? TokenChannel.EMAIL : TokenChannel.SMS;
        }

        TokenChannel channel = TokenChannel.valueOf(requestedChannel.trim().toUpperCase(Locale.ROOT));
        if (channel == TokenChannel.EMAIL && (request.email() == null || request.email().isBlank())) {
            throw new DomainException("Email is required when verificationChannel is EMAIL");
        }
        return channel;
    }

    private LoginResponse issueTokens(User user, boolean rememberMe) {
        List<String> roles = getUserRoles(user.getId());
        List<String> permissions = getPermissions(roles);
        String accessToken = jwtService.createAccessToken(user.getId(), roles, permissions, accessTtl);
        long refreshTtl = rememberMe ? rememberRefreshTtl : this.refreshTtl;
        RefreshToken refreshToken = tokenService.createRefreshToken(user.getId(), refreshTtl);
        return new LoginResponse(accessToken, accessTtl, refreshToken.getToken(), refreshTtl, false);
    }

    private List<String> getUserRoles(UUID userId) {
        return userRoleRepository.findByUserId(userId).stream()
                .map(UserRole::getRoleId)
                .map(roleId -> roleRepository.findById(roleId).orElseThrow(() -> new DomainException("Role not found")))
                .map(Role::getName)
                .toList();
    }

    private List<String> getPermissions(List<String> roleNames) {
        return roleNames.stream()
                .map(name -> roleRepository.findByName(name).orElseThrow(() -> new DomainException("Role not found")))
                .flatMap(role -> rolePermissionRepository.findByRoleId(role.getId()).stream())
                .map(RolePermission::getPermissionId)
                .map(id -> permissionRepository.findById(id).orElseThrow(() -> new DomainException("Permission not found")))
                .map(Permission::getName)
                .distinct()
                .toList();
    }

    private void validateUniqueness(RegisterRequest request) {
        if (userRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new DomainException("Username already used");
        }
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new DomainException("Phone number already used");
        }
        if (request.email() != null && !request.email().isBlank() && userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new DomainException("Email already used");
        }
    }

    private java.util.Optional<User> resolveByIdentifier(String identifier) {
        String value = identifier.trim();
        if (IdentifierUtils.isPhone(value)) {
            return userRepository.findByPhoneNumber(value);
        }
        if (IdentifierUtils.isEmail(value)) {
            return userRepository.findByEmailIgnoreCase(value);
        }
        return userRepository.findByUsernameIgnoreCase(value);
    }

    private Role resolveDefaultRole(String accountType) {
        String roleName = switch (accountType) {
            case "SUPPLIER", "PRODUCER" -> "SUPPLIER";
            default -> "CONSUMER";
        };
        return roleRepository.findByName(roleName).orElseThrow(() -> new DomainException("Role seed missing: " + roleName));
    }

    private User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new DomainException("Unauthenticated");
        }
        UUID userId = UUID.fromString(authentication.getName());
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
