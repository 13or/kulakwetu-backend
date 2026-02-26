package com.kulakwetu.agrisol.service;

import com.kulakwetu.agrisol.dto.*;
import com.kulakwetu.agrisol.entity.*;
import com.kulakwetu.agrisol.enums.UserProductStatus;
import com.kulakwetu.agrisol.repository.*;
import com.kulakwetu.common.exception.DomainException;
import com.kulakwetu.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgrisolCatalogService {

    private final ProductCategoryRepository productCategoryRepository;
    private final AgrisolProductRepository agrisolProductRepository;
    private final SupplierBusinessRepository supplierBusinessRepository;
    private final UserProductRepository userProductRepository;
    private final UserProductMediaRepository userProductMediaRepository;
    private final UserProductPriceRepository userProductPriceRepository;

    @Transactional
    public ProductCategory createCategory(ProductCategoryRequest req) {
        return productCategoryRepository.save(ProductCategory.builder().id(UUID.randomUUID())
                .imageUrl(req.imageUrl()).name(req.name()).description(req.description()).isPublic(req.isPublic()).build());
    }

    @Transactional
    public AgrisolProduct createAgrisolProduct(AgrisolProductRequest req) {
        ProductCategory category = productCategoryRepository.findById(req.productCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return agrisolProductRepository.save(AgrisolProduct.builder().id(UUID.randomUUID())
                .imageUrl(req.imageUrl()).name(req.name()).productCategory(category).isPublic(req.isPublic()).isPopular(req.isPopular()).build());
    }

    @Transactional
    public SupplierBusiness upsertSupplierBusiness(SupplierBusinessRequest req) {
        UUID supplierId = currentUserId();
        SupplierBusiness e = supplierBusinessRepository.findBySupplierUserId(supplierId)
                .orElseGet(() -> SupplierBusiness.builder().id(UUID.randomUUID()).supplierUserId(supplierId).build());
        e.setBusinessName(req.businessName());
        e.setIdnat(req.idnat()); e.setRccm(req.rccm()); e.setTaxNumber(req.taxNumber());
        e.setLocationAddress(req.locationAddress()); e.setProductionAddress(req.productionAddress());
        e.setLogo(req.logo()); e.setPhone(req.phone()); e.setWhatsapp(req.whatsapp()); e.setEmail(req.email());
        e.setFacebook(req.facebook()); e.setLinkedin(req.linkedin()); e.setInstagram(req.instagram()); e.setTiktok(req.tiktok()); e.setYoutube(req.youtube());
        return supplierBusinessRepository.save(e);
    }

    @Transactional
    public UserProductResponse createUserProduct(UserProductRequest req) {
        UUID supplierId = currentUserId();
        AgrisolProduct base = agrisolProductRepository.findById(req.agrisolProductId()).orElseThrow(() -> new ResourceNotFoundException("AgrisolProduct not found"));
        UserProduct p = userProductRepository.save(UserProduct.builder().id(UUID.randomUUID())
                .supplierUserId(supplierId).agrisolProduct(base).name(req.name()).description(req.description())
                .inStock(req.inStock()).quantityInStock(req.quantityInStock()).reservedQuantity(java.math.BigDecimal.ZERO).stockMeasureUnit(req.stockMeasureUnit())
                .available(req.available()).availableAt(req.availableAt()).productionSpace(req.productionSpace())
                .productionSpaceUnit(req.productionSpaceUnit()).productionLocation(req.productionLocation())
                .isPopular(req.isPopular()).status(UserProductStatus.PENDING).build());
        return toUserProductResponse(p);
    }

    @Transactional
    public UserProductResponse updateUserProduct(UUID userProductId, UserProductRequest req) {
        UUID supplierId = currentUserId();
        UserProduct p = findSupplierOwnedProduct(userProductId, supplierId);
        AgrisolProduct base = agrisolProductRepository.findById(req.agrisolProductId()).orElseThrow(() -> new ResourceNotFoundException("AgrisolProduct not found"));
        p.setAgrisolProduct(base); p.setName(req.name()); p.setDescription(req.description());
        p.setInStock(req.inStock()); p.setQuantityInStock(req.quantityInStock()); p.setStockMeasureUnit(req.stockMeasureUnit());
        p.setAvailable(req.available()); p.setAvailableAt(req.availableAt()); p.setProductionSpace(req.productionSpace());
        p.setProductionSpaceUnit(req.productionSpaceUnit()); p.setProductionLocation(req.productionLocation()); p.setPopular(req.isPopular());
        if (p.getStatus() != UserProductStatus.PENDING) p.setStatus(UserProductStatus.PENDING);
        return toUserProductResponse(userProductRepository.save(p));
    }

    @Transactional
    public void addMedia(UUID userProductId, UserProductMediaRequest req) {
        UUID supplierId = currentUserId();
        UserProduct p = findSupplierOwnedProduct(userProductId, supplierId);
        userProductMediaRepository.save(UserProductMedia.builder().id(UUID.randomUUID()).userProduct(p).mediaUrl(req.mediaUrl()).featured(req.featured()).type(req.type()).build());
    }

    @Transactional
    public void addPrice(UUID userProductId, UserProductPriceRequest req) {
        UUID supplierId = currentUserId();
        UserProduct p = findSupplierOwnedProduct(userProductId, supplierId);
        userProductPriceRepository.save(UserProductPrice.builder().id(UUID.randomUUID()).userProduct(p).unit(req.unit()).price(req.price()).currencyCode(req.currencyCode()).build());
    }

    @Transactional(readOnly = true)
    public List<UserProductResponse> myProducts() {
        UUID supplierId = currentUserId();
        return userProductRepository.findBySupplierUserIdOrderByNameAsc(supplierId).stream().map(this::toUserProductResponse).toList();
    }

    @Transactional
    public UserProductResponse updateStatus(UUID userProductId, UserProductStatusRequest req) {
        UserProduct p = userProductRepository.findById(userProductId).orElseThrow(() -> new ResourceNotFoundException("UserProduct not found"));
        p.setStatus(req.status());
        return toUserProductResponse(userProductRepository.save(p));
    }

    @Transactional(readOnly = true)
    public List<UserProductResponse> pendingProducts() {
        return userProductRepository.findByStatusAndAgrisolProductIsPublicTrueOrderByNameAsc(UserProductStatus.PENDING).stream().map(this::toUserProductResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ShopProductResponse> shopProducts() {
        return userProductRepository.findByStatusAndAgrisolProductIsPublicTrueOrderByNameAsc(UserProductStatus.APPROVED).stream()
                .filter(up -> up.getAgrisolProduct().isPublic())
                .map(up -> new ShopProductResponse(
                        up.getId(), up.getName(), up.getDescription(), up.getAgrisolProduct().getName(),
                        userProductMediaRepository.findByUserProductId(up.getId()).stream().map(UserProductMedia::getMediaUrl).toList(),
                        userProductPriceRepository.findByUserProductId(up.getId()).stream()
                                .map(p -> new ShopProductResponse.PriceView(p.getUnit(), p.getPrice(), p.getCurrencyCode())).toList()
                )).toList();
    }

    private UserProduct findSupplierOwnedProduct(UUID id, UUID supplierId) {
        UserProduct p = userProductRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("UserProduct not found"));
        if (!p.getSupplierUserId().equals(supplierId)) throw new DomainException("Not your product");
        return p;
    }

    private UserProductResponse toUserProductResponse(UserProduct p) {
        return new UserProductResponse(p.getId(), p.getSupplierUserId(), p.getAgrisolProduct().getId(), p.getName(), p.getDescription(), p.getStatus());
    }

    private UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) throw new DomainException("Unauthenticated");
        return UUID.fromString(auth.getName());
    }
}
