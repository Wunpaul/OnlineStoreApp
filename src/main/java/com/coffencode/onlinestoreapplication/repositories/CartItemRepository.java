package com.coffencode.onlinestoreapplication.repositories;

import com.coffencode.onlinestoreapplication.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
