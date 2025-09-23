package com.coffencode.onlinestoreapplication.service;

import com.coffencode.onlinestoreapplication.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO createOrderFromCart(Long customerId);
    OrderDTO getOrderById(Long orderId);
    List<OrderDTO> getOrdersByCustomerId(Long customerId);
    OrderDTO cancelOrder(Long orderId);
}
