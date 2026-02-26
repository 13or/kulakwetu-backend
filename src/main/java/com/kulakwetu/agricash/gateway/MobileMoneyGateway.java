package com.kulakwetu.agricash.gateway;

import java.math.BigDecimal;
import java.util.UUID;

public interface MobileMoneyGateway {
    String payout(UUID supplierUserId, BigDecimal amount, String currencyCode, String payoutMethod, String payoutAccount);
}
