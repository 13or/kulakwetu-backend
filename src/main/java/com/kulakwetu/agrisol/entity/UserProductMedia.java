package com.kulakwetu.agrisol.entity;

import com.kulakwetu.agrisol.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ag_user_product_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProductMedia {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_product_id", nullable = false)
    private UserProduct userProduct;

    @Column(name = "media_url", nullable = false, length = 255)
    private String mediaUrl;

    @Column(nullable = false)
    private boolean featured;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MediaType type;
}
