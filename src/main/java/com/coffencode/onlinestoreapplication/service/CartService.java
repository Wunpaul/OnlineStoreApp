package com.coffencode.onlinestoreapplication.service;

import com.coffencode.onlinestoreapplication.dto.CartDTO;

public interface CartService {
    CartDTO addItemToCartByEmail(String email, Long productId, int quantity);
    CartDTO getCartByCustomerEmail(String email);
    CartDTO createCartForCustomerEmail(String email);
    void clearCartByCustomerEmail(String email);
}
