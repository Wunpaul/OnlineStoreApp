package com.coffencode.onlinestoreapplication.repositories;

import com.coffencode.onlinestoreapplication.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
