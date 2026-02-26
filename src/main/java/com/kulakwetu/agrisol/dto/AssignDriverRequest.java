package com.kulakwetu.agrisol.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignDriverRequest(@NotNull UUID driverUserId) {}
