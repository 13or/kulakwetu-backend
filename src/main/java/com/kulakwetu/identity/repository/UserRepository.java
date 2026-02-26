package com.kulakwetu.identity.repository;

import com.kulakwetu.identity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmailIgnoreCase(String email);

    @Query("select u from User u where upper(u.accountType) in ('SUPPLIER', 'PRODUCER')")
    Page<User> findSuppliers(Pageable pageable);

    @Query("select u from User u where upper(u.accountType) = 'CONSUMER'")
    Page<User> findConsumers(Pageable pageable);
}
