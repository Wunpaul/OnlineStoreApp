package com.coffencode.onlinestoreapplication.mapper;

import com.coffencode.onlinestoreapplication.dto.OrderItemDTO;
import com.coffencode.onlinestoreapplication.entities.OrderItem;

public class OrderItemMapper {
    public static OrderItemDTO toDTO(OrderItem item) {
        if (item == null) return null;
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setOrderId(item.getOrder() != null ? item.getOrder().getId() : null);
        dto.setProductId(item.getProduct() != null ? item.getProduct().getId() : null);
        dto.setProductName(item.getProduct() != null ? item.getProduct().getName() : null);
        dto.setProductPrice(item.getPrice() != null ? item.getPrice() : 0.0);
        dto.setQuantity(item.getQuantity());
        return dto;
    }
}
