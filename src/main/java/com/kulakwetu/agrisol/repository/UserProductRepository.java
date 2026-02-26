package com.kulakwetu.agrisol.repository;

import com.kulakwetu.agrisol.entity.UserProduct;
import com.kulakwetu.agrisol.enums.UserProductStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProductRepository extends JpaRepository<UserProduct, UUID> {
    List<UserProduct> findBySupplierUserIdOrderByNameAsc(UUID supplierUserId);
    List<UserProduct> findByStatusAndAgrisolProductIsPublicTrueOrderByNameAsc(UserProductStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from UserProduct p where p.id = :id")
    Optional<UserProduct> findByIdForUpdate(UUID id);
}
