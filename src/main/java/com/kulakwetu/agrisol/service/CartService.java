package com.kulakwetu.agrisol.service;

import com.kulakwetu.agrisol.dto.CartItemRequest;
import com.kulakwetu.agrisol.dto.CartItemResponse;
import com.kulakwetu.agrisol.entity.Cart;
import com.kulakwetu.agrisol.entity.CartItem;
import com.kulakwetu.agrisol.entity.UserProduct;
import com.kulakwetu.agrisol.enums.CartStatus;
import com.kulakwetu.agrisol.repository.CartItemRepository;
import com.kulakwetu.agrisol.repository.CartRepository;
import com.kulakwetu.agrisol.repository.UserProductRepository;
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
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserProductRepository userProductRepository;

    @Transactional
    public List<CartItemResponse> getMyCartItems() {
        Cart cart = activeCart(currentUserId());
        return cartItemRepository.findByCartId(cart.getId()).stream()
                .map(i -> new CartItemResponse(i.getId(), i.getUserProduct().getId(), i.getUserProduct().getName(), i.getQuantity()))
                .toList();
    }

    @Transactional
    public List<CartItemResponse> addOrUpdateItem(CartItemRequest req) {
        UUID userId = currentUserId();
        Cart cart = activeCart(userId);
        UserProduct userProduct = userProductRepository.findById(req.userProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem item = cartItemRepository.findByCartIdAndUserProductId(cart.getId(), req.userProductId())
                .orElseGet(() -> CartItem.builder().id(UUID.randomUUID()).cart(cart).userProduct(userProduct).build());
        item.setQuantity(req.quantity());
        cartItemRepository.save(item);
        return getMyCartItems();
    }

    @Transactional
    public void removeItem(UUID itemId) {
        UUID userId = currentUserId();
        Cart cart = activeCart(userId);
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (!item.getCart().getId().equals(cart.getId())) throw new ResourceNotFoundException("Cart item not found");
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearMyCart() {
        Cart cart = activeCart(currentUserId());
        cartItemRepository.deleteByCartId(cart.getId());
    }

    @Transactional
    public Cart activeCart(UUID userId) {
        return cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> cartRepository.save(Cart.builder().id(UUID.randomUUID()).userId(userId).status(CartStatus.ACTIVE).build()));
    }

    UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(auth.getName());
    }
}
