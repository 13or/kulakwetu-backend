package com.kulakwetu.agrisol.service;

import com.kulakwetu.agrisol.dto.CheckoutRequest;
import com.kulakwetu.agrisol.dto.OrderSummaryResponse;
import com.kulakwetu.agrisol.entity.*;
import com.kulakwetu.agrisol.enums.CartStatus;
import com.kulakwetu.agrisol.enums.OrderStatus;
import com.kulakwetu.agrisol.enums.PaymentMethod;
import com.kulakwetu.agrisol.enums.UserProductStatus;
import com.kulakwetu.agrisol.repository.*;
import com.kulakwetu.common.exception.DomainException;
import com.kulakwetu.common.exception.ResourceNotFoundException;
import com.kulakwetu.geo.repository.CityRepository;
import com.kulakwetu.geo.repository.CountryRepository;
import com.kulakwetu.geo.repository.MunicipalityRepository;
import com.kulakwetu.geo.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserProductRepository userProductRepository;
    private final UserProductPriceRepository userProductPriceRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final CityRepository cityRepository;
    private final MunicipalityRepository municipalityRepository;
    private final DeliveryFeeService deliveryFeeService;

    @Transactional
    public OrderSummaryResponse checkout(CheckoutRequest req) {
        UUID userId = currentUserId();
        Cart cart = cartService.activeCart(userId);
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) throw new DomainException("Cart is empty");

        BigDecimal subtotal = BigDecimal.ZERO;
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .paymentMethod(req.paymentMethod())
                .currencyCode(req.currencyCode())
                .status(req.paymentMethod() == PaymentMethod.CASH_ON_DELIVERY ? OrderStatus.CREATED : OrderStatus.PENDING_PAYMENT)
                .subtotal(BigDecimal.ZERO)
                .deliveryFee(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();
        order = orderRepository.save(order);

        for (CartItem item : cartItems) {
            UserProduct locked = userProductRepository.findByIdForUpdate(item.getUserProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            if (locked.getStatus() != UserProductStatus.APPROVED || !locked.isAvailable()) {
                throw new DomainException("Product not available for order");
            }
            BigDecimal available = value(locked.getQuantityInStock()).subtract(value(locked.getReservedQuantity()));
            if (available.compareTo(item.getQuantity()) < 0) throw new DomainException("Insufficient stock");

            if (req.paymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
                locked.setQuantityInStock(value(locked.getQuantityInStock()).subtract(item.getQuantity()));
            } else {
                locked.setReservedQuantity(value(locked.getReservedQuantity()).add(item.getQuantity()));
            }
            userProductRepository.save(locked);

            UserProductPrice firstPrice = userProductPriceRepository.findByUserProductId(locked.getId()).stream().findFirst()
                    .orElseThrow(() -> new DomainException("Price not configured for product " + locked.getName()));
            BigDecimal lineTotal = firstPrice.getPrice().multiply(item.getQuantity());
            subtotal = subtotal.add(lineTotal);

            orderItemRepository.save(OrderItem.builder()
                    .id(UUID.randomUUID())
                    .order(order)
                    .userProduct(locked)
                    .quantity(item.getQuantity())
                    .unitPrice(firstPrice.getPrice())
                    .lineTotal(lineTotal)
                    .currencyCode(firstPrice.getCurrencyCode())
                    .build());
        }

        BigDecimal fee = deliveryFeeService.compute(req.countryId(), req.provinceId(), req.cityId(), req.municipalityId());
        order.setSubtotal(subtotal);
        order.setDeliveryFee(fee);
        order.setTotal(subtotal.add(fee));
        orderRepository.save(order);

        deliveryAddressRepository.save(DeliveryAddress.builder()
                .id(UUID.randomUUID())
                .order(order)
                .country(countryRepository.findById(req.countryId()).orElseThrow(() -> new ResourceNotFoundException("Country not found")))
                .province(provinceRepository.findById(req.provinceId()).orElseThrow(() -> new ResourceNotFoundException("Province not found")))
                .city(cityRepository.findById(req.cityId()).orElseThrow(() -> new ResourceNotFoundException("City not found")))
                .municipality(municipalityRepository.findById(req.municipalityId()).orElseThrow(() -> new ResourceNotFoundException("Municipality not found")))
                .addressLine(req.addressLine())
                .contactName(req.contactName())
                .contactPhone(req.contactPhone())
                .notes(req.notes())
                .build());

        cartItemRepository.deleteByCartId(cart.getId());
        cart.setStatus(CartStatus.CHECKED_OUT);
        cartRepository.save(cart);

        return toSummary(order);
    }

    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> myOrders() {
        return orderRepository.findByUserIdOrderByIdDesc(currentUserId()).stream().map(this::toSummary).toList();
    }

    @Transactional
    public void cancelMyOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUserId().equals(currentUserId())) throw new DomainException("Forbidden");
        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) return;

        if (order.getPaymentMethod() == PaymentMethod.AGRICASH) {
            releaseReserved(order);
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private void releaseReserved(Order order) {
        for (OrderItem item : orderItemRepository.findByOrderId(order.getId())) {
            UserProduct locked = userProductRepository.findByIdForUpdate(item.getUserProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            locked.setReservedQuantity(value(locked.getReservedQuantity()).subtract(item.getQuantity()).max(BigDecimal.ZERO));
            userProductRepository.save(locked);
        }
    }

    private BigDecimal value(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }

    private OrderSummaryResponse toSummary(Order o) {
        return new OrderSummaryResponse(o.getId(), o.getStatus(), o.getPaymentMethod(), o.getCurrencyCode(), o.getSubtotal(), o.getDeliveryFee(), o.getTotal(), o.getAssignedDriverUserId());
    }

    private UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(auth.getName());
    }
}
