package com.coffencode.onlinestoreapplication.service;

import com.coffencode.onlinestoreapplication.dto.CartDTO;

public interface CartService {
    CartDTO createCart(Long customerId);
    CartDTO getCartByCustomer(Long customerId);
    void clearCart(Long cartId);
    CartDTO addItemToCart(Long customerId, Long productId, Integer quantity);
}
