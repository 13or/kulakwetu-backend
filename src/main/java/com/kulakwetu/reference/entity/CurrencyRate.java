package com.kulakwetu.reference.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ref_currency_rate", uniqueConstraints = @UniqueConstraint(name = "uk_ref_currency_rate_pair_time", columnNames = {"base_currency_id", "quote_currency_id", "valid_from"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyRate {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "base_currency_id", nullable = false)
    private Currency baseCurrency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quote_currency_id", nullable = false)
    private Currency quoteCurrency;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal rate;

    @Column(name = "valid_from", nullable = false)
    private OffsetDateTime validFrom;
}
