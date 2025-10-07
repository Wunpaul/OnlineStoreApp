package com.coffencode.onlinestoreapplication.controllers;

import com.coffencode.onlinestoreapplication.dto.AddCartItemDTO;
import com.coffencode.onlinestoreapplication.dto.CartDTO;
import com.coffencode.onlinestoreapplication.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) { this.cartService = cartService; }

    // Add item to currently authenticated user's cart
    @PostMapping("/items")
    public ResponseEntity<CartDTO> addItemToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddCartItemDTO request) {

        String email = userDetails.getUsername();
        CartDTO updated = cartService.addItemToCartByEmail(email, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updated);
    }

    // Get current user's cart
    @GetMapping("/me")
    public ResponseEntity<CartDTO> getMyCart(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(cartService.getCartByCustomerEmail(email));
    }

    // Create cart for current user (if you want explicit creation)
    @PostMapping("/me")
    public ResponseEntity<CartDTO> createCartForMe(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(cartService.createCartForCustomerEmail(email));
    }

    // Clear current user's cart
    @DeleteMapping("/me")
    public ResponseEntity<Void> clearMyCart(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        cartService.clearCartByCustomerEmail(email);
        return ResponseEntity.noContent().build();
    }

}
