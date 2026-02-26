package com.kulakwetu.agrisol.service;

import com.kulakwetu.agrisol.dto.OrderSummaryResponse;
import com.kulakwetu.agrisol.entity.Order;
import com.kulakwetu.agrisol.enums.OrderStatus;
import com.kulakwetu.agrisol.repository.OrderRepository;
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
public class AssignmentService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> managerOrders() {
        return orderRepository.findByStatusOrderByIdDesc(OrderStatus.CREATED).stream().map(this::toSummary).toList();
    }

    @Transactional
    public OrderSummaryResponse assignDriver(UUID orderId, UUID driverUserId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new DomainException("Order cannot be assigned");
        }
        order.setAssignedDriverUserId(driverUserId);
        order.setStatus(OrderStatus.ASSIGNED);
        return toSummary(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> myDriverOrders() {
        return orderRepository.findByAssignedDriverUserIdOrderByIdDesc(currentUserId()).stream().map(this::toSummary).toList();
    }

    @Transactional
    public OrderSummaryResponse markDelivered(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        UUID me = currentUserId();
        if (order.getAssignedDriverUserId() == null || !order.getAssignedDriverUserId().equals(me)) throw new DomainException("Not assigned to you");
        order.setStatus(OrderStatus.DELIVERED);
        return toSummary(orderRepository.save(order));
    }

    private OrderSummaryResponse toSummary(Order o) {
        return new OrderSummaryResponse(o.getId(), o.getStatus(), o.getPaymentMethod(), o.getCurrencyCode(), o.getSubtotal(), o.getDeliveryFee(), o.getTotal(), o.getAssignedDriverUserId());
    }

    private UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(auth.getName());
    }
}
