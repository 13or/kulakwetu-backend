package com.kulakwetu.admin.service;

import com.kulakwetu.admin.dto.*;
import com.kulakwetu.admin.entity.CompanyInfo;
import com.kulakwetu.admin.entity.Slide;
import com.kulakwetu.admin.entity.SubscriptionType;
import com.kulakwetu.admin.enums.SlideTarget;
import com.kulakwetu.admin.repository.CompanyInfoRepository;
import com.kulakwetu.admin.repository.SlideRepository;
import com.kulakwetu.admin.repository.SubscriptionTypeRepository;
import com.kulakwetu.common.exception.DomainException;
import com.kulakwetu.common.exception.ResourceNotFoundException;
import com.kulakwetu.identity.entity.User;
import com.kulakwetu.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CompanyInfoRepository companyInfoRepository;
    private final SlideRepository slideRepository;
    private final SubscriptionTypeRepository subscriptionTypeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CompanyInfoResponse getCompanyInfo() {
        return companyInfoRepository.findAll().stream().findFirst().map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Company information not found"));
    }

    @Transactional
    public CompanyInfoResponse upsertCompanyInfo(CompanyInfoRequest request) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        CompanyInfo info = companyInfoRepository.findAll().stream().findFirst().orElse(CompanyInfo.builder()
                .id(UUID.randomUUID())
                .build());

        info.setLegalName(request.legalName());
        info.setTradeName(request.tradeName());
        info.setSupportEmail(request.supportEmail());
        info.setSupportPhone(request.supportPhone());
        info.setWebsiteUrl(request.websiteUrl());
        info.setAgrisolDescription(request.agrisolDescription());
        info.setAgricashDescription(request.agricashDescription());
        info.setUpdatedAt(now);

        return toResponse(companyInfoRepository.save(info));
    }

    @Transactional
    public SlideResponse createSlide(SlideRequest request) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        Slide slide = Slide.builder()
                .id(UUID.randomUUID())
                .target(request.target())
                .imageUrl(request.imageUrl())
                .heading(request.heading())
                .text(request.text())
                .description(request.description())
                .slug(request.slug())
                .isPublic(request.isPublic())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return toResponse(slideRepository.save(slide));
    }

    @Transactional
    public SlideResponse updateSlide(UUID id, SlideRequest request) {
        Slide slide = slideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Slide not found"));

        slide.setTarget(request.target());
        slide.setImageUrl(request.imageUrl());
        slide.setHeading(request.heading());
        slide.setText(request.text());
        slide.setDescription(request.description());
        slide.setSlug(request.slug());
        slide.setPublic(request.isPublic());
        slide.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        return toResponse(slideRepository.save(slide));
    }

    @Transactional(readOnly = true)
    public SlideResponse getSlide(UUID id) {
        return toResponse(slideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Slide not found")));
    }

    @Transactional(readOnly = true)
    public Page<SlideResponse> listSlides(SlideTarget target, Pageable pageable) {
        if (target == null) {
            return slideRepository.findAll(pageable).map(this::toResponse);
        }
        return slideRepository.findByTargetOrderByCreatedAtDesc(target, pageable).map(this::toResponse);
    }

    @Transactional
    public void deleteSlide(UUID id) {
        slideRepository.delete(slideRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Slide not found")));
    }

    @Transactional
    public SubscriptionTypeResponse createSubscription(SubscriptionTypeRequest request) {
        validateDuration(request.durationDays());
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        SubscriptionType subscriptionType = SubscriptionType.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .benefits(request.benefits())
                .price(request.price())
                .currencyCode(request.currencyCode().toUpperCase())
                .durationDays(request.durationDays())
                .isActive(request.isActive())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return toResponse(subscriptionTypeRepository.save(subscriptionType));
    }

    @Transactional
    public SubscriptionTypeResponse updateSubscription(UUID id, SubscriptionTypeRequest request) {
        validateDuration(request.durationDays());
        SubscriptionType subscriptionType = subscriptionTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription type not found"));

        subscriptionType.setName(request.name());
        subscriptionType.setBenefits(request.benefits());
        subscriptionType.setPrice(request.price());
        subscriptionType.setCurrencyCode(request.currencyCode().toUpperCase());
        subscriptionType.setDurationDays(request.durationDays());
        subscriptionType.setActive(request.isActive());
        subscriptionType.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        return toResponse(subscriptionTypeRepository.save(subscriptionType));
    }

    @Transactional(readOnly = true)
    public SubscriptionTypeResponse getSubscription(UUID id) {
        return toResponse(subscriptionTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription type not found")));
    }

    @Transactional(readOnly = true)
    public Page<SubscriptionTypeResponse> listSubscriptions(Pageable pageable) {
        return subscriptionTypeRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional
    public void deleteSubscription(UUID id) {
        subscriptionTypeRepository.delete(subscriptionTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription type not found")));
    }

    @Transactional(readOnly = true)
    public Page<AdminUserResponse> listSuppliers(Pageable pageable) {
        return userRepository.findSuppliers(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AdminUserResponse> listConsumers(Pageable pageable) {
        return userRepository.findConsumers(pageable).map(this::toResponse);
    }

    @Transactional
    public AdminUserResponse updateUserActivation(UUID userId, boolean enabled) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!"SUPPLIER".equalsIgnoreCase(user.getAccountType())
                && !"PRODUCER".equalsIgnoreCase(user.getAccountType())
                && !"CONSUMER".equalsIgnoreCase(user.getAccountType())) {
            throw new DomainException("Only supplier and consumer accounts can be managed from admin users module");
        }

        user.setEnabled(enabled);
        user.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return toResponse(userRepository.save(user));
    }

    private void validateDuration(Integer durationDays) {
        if (durationDays == null || durationDays <= 0) {
            throw new DomainException("durationDays must be greater than zero");
        }
    }

    private CompanyInfoResponse toResponse(CompanyInfo companyInfo) {
        return new CompanyInfoResponse(companyInfo.getId(), companyInfo.getLegalName(), companyInfo.getTradeName(),
                companyInfo.getSupportEmail(), companyInfo.getSupportPhone(), companyInfo.getWebsiteUrl(),
                companyInfo.getAgrisolDescription(), companyInfo.getAgricashDescription(), companyInfo.getUpdatedAt());
    }

    private SlideResponse toResponse(Slide slide) {
        return new SlideResponse(slide.getId(), slide.getTarget(), slide.getImageUrl(), slide.getHeading(),
                slide.getText(), slide.getDescription(), slide.getSlug(), slide.isPublic(), slide.getCreatedAt(), slide.getUpdatedAt());
    }

    private SubscriptionTypeResponse toResponse(SubscriptionType subscriptionType) {
        return new SubscriptionTypeResponse(subscriptionType.getId(), subscriptionType.getName(), subscriptionType.getBenefits(),
                subscriptionType.getPrice(), subscriptionType.getCurrencyCode(), subscriptionType.getDurationDays(),
                subscriptionType.isActive(), subscriptionType.getCreatedAt(), subscriptionType.getUpdatedAt());
    }

    private AdminUserResponse toResponse(User user) {
        return new AdminUserResponse(user.getId(), user.getUsername(), user.getPhoneNumber(), user.getEmail(),
                user.getFirstName(), user.getLastName(), user.getAccountType(), user.isEnabled(), user.isVerified(), user.getCreatedAt());
    }
}
