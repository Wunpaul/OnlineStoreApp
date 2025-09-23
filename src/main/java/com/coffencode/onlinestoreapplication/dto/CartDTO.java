package com.coffencode.onlinestoreapplication.dto;

import java.util.List;

public class CartDTO {
    private Long id;
    private Long customerId;
    private List<CartItemDTO> cartItems;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public List<CartItemDTO> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItemDTO> cartItems) { this.cartItems = cartItems; }
}
