package com.kulakwetu.agrisol.entity;

import com.kulakwetu.geo.entity.City;
import com.kulakwetu.geo.entity.Country;
import com.kulakwetu.geo.entity.Municipality;
import com.kulakwetu.geo.entity.Province;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ag_delivery_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryAddress {
    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;

    @Column(name = "address_line", nullable = false, length = 255)
    private String addressLine;

    @Column(name = "contact_name", nullable = false, length = 120)
    private String contactName;

    @Column(name = "contact_phone", nullable = false, length = 30)
    private String contactPhone;

    @Column(length = 500)
    private String notes;
}
