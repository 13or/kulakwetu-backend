package com.kulakwetu.identity.controller;

import com.kulakwetu.identity.dto.*;
import com.kulakwetu.identity.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register account and auto-create Agricash wallet")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verify(@Valid @RequestBody VerifyAccountRequest request) {
        authService.verifyAccount(request);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return authService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
    }

    @PostMapping("/mfa/setup")
    @PreAuthorize("isAuthenticated()")
    public MfaSetupResponse mfaSetup() {
        return authService.beginMfaSetup();
    }

    @PostMapping("/mfa/confirm")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void mfaConfirm(@Valid @RequestBody MfaConfirmRequest request) {
        authService.confirmMfa(request);
    }

    @PostMapping("/mfa/verify")
    public LoginResponse mfaVerify(@Valid @RequestBody MfaVerifyRequest request) {
        return authService.verifyMfa(request);
    }
}
