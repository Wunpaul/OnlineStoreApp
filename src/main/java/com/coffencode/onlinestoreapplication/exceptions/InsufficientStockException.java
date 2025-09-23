package com.coffencode.onlinestoreapplication.exceptions;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long productId) {
        super("Insufficient stock for product id " + productId);
    }
}
