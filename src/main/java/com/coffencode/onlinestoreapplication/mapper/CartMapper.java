package com.coffencode.onlinestoreapplication.mapper;

import com.coffencode.onlinestoreapplication.dto.CartDTO;
import com.coffencode.onlinestoreapplication.entities.Cart;

import java.util.stream.Collectors;

public class CartMapper {
    public static CartDTO toDTO(Cart cart) {
        if (cart == null) return null;
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setCustomerId(cart.getCustomer() != null ? cart.getCustomer().getId() : null);
        dto.setCartItems(cart.getCartItems() == null ? null :
                cart.getCartItems().stream().map(CartItemMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }
}
