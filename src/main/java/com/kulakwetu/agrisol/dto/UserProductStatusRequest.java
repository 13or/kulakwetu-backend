package com.kulakwetu.agrisol.dto;

import com.kulakwetu.agrisol.enums.UserProductStatus;
import jakarta.validation.constraints.NotNull;

public record UserProductStatusRequest(@NotNull UserProductStatus status) {}
