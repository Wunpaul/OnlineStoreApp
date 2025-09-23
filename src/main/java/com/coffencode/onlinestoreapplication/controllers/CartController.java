package com.coffencode.onlinestoreapplication.controllers;

import com.coffencode.onlinestoreapplication.dto.AddCartItemDTO;
import com.coffencode.onlinestoreapplication.dto.CartDTO;
import com.coffencode.onlinestoreapplication.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) { this.cartService = cartService; }

    @PostMapping("/{customerId}/items")
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable Long customerId,
                                                 @Valid @RequestBody AddCartItemDTO request) {
        CartDTO updated = cartService.addItemToCart(customerId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long customerId) {
        return ResponseEntity.ok(cartService.getCartByCustomer(customerId));
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<CartDTO> createCart(@PathVariable Long customerId) {
        return ResponseEntity.ok(cartService.createCart(customerId));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
