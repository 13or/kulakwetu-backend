package com.kulakwetu.agricash.dto;

import java.math.BigDecimal;

public record WalletBalanceResponse(String currencyCode, BigDecimal availableAmount, BigDecimal reservedAmount) {}
