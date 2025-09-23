package com.coffencode.onlinestoreapplication.mapper;

import com.coffencode.onlinestoreapplication.dto.OrderDTO;
import com.coffencode.onlinestoreapplication.entities.Order;

import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDTO toDTO(Order order) {
        if (order == null) return null;
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomer() != null ? order.getCustomer().getId() : null);
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setOrderItems(order.getOrderItems() == null ? null :
                order.getOrderItems().stream().map(OrderItemMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }
}
