package com.coffencode.onlinestoreapplication.mapper;

import com.coffencode.onlinestoreapplication.dto.CartItemDTO;
import com.coffencode.onlinestoreapplication.entities.CartItem;

public class CartItemMapper {
    public static CartItemDTO toDTO(CartItem item) {
        if (item == null) return null;
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setProductPrice(item.getProduct().getPrice());
        }
        return dto;
    }
}
