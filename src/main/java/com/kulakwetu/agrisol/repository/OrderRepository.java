package com.kulakwetu.agrisol.repository;

import com.kulakwetu.agrisol.entity.Order;
import com.kulakwetu.agrisol.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserIdOrderByIdDesc(UUID userId);
    List<Order> findByStatusOrderByIdDesc(OrderStatus status);
    List<Order> findByAssignedDriverUserIdOrderByIdDesc(UUID assignedDriverUserId);
}
