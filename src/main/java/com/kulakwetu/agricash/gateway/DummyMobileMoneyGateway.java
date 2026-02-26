package com.kulakwetu.agricash.gateway;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class DummyMobileMoneyGateway implements MobileMoneyGateway {
    @Override
    public String payout(UUID supplierUserId, BigDecimal amount, String currencyCode, String payoutMethod, String payoutAccount) {
        return "MM-" + UUID.randomUUID();
    }
}
