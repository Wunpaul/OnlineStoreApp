package com.coffencode.onlinestoreapplication.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long id) {
        super("Cart not found for customer id " + id);
    }
}
