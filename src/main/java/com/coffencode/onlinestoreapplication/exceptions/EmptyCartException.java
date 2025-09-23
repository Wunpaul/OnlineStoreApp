package com.coffencode.onlinestoreapplication.exceptions;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException(Long customerId) {
        super("Cannot place order: cart is empty for customer " + customerId);
    }
}
