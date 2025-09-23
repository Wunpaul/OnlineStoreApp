package com.coffencode.onlinestoreapplication.controllers;

import com.coffencode.onlinestoreapplication.dto.OrderDTO;
import com.coffencode.onlinestoreapplication.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) { this.service = service; }

    // checkout/cart -> order
    @PostMapping("/checkout/{customerId}")
    public ResponseEntity<OrderDTO> checkout(@PathVariable Long customerId) {
        OrderDTO created = service.createOrderFromCart(customerId);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getOrdersByCustomerId(customerId));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancel(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.cancelOrder(orderId));
    }
}
