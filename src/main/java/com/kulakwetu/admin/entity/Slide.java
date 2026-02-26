package com.kulakwetu.admin.entity;

import com.kulakwetu.admin.enums.SlideTarget;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "adm_slide")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slide {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SlideTarget target;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(nullable = false, length = 160)
    private String heading;

    @Column(name = "body_text", length = 255)
    private String text;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, unique = true, length = 160)
    private String slug;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
