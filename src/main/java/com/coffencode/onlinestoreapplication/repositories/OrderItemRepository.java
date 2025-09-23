package com.coffencode.onlinestoreapplication.repositories;

import com.coffencode.onlinestoreapplication.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
