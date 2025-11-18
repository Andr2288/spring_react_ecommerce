package com.lab.springecommerce.controller;

/*
    @project   spring-ecommerce
    @class     CartController
    @version   1.0.0
    @since     17.11.2025 - 16:49
*/

import com.lab.springecommerce.dto.AddToCartRequest;
import com.lab.springecommerce.dto.CartItemResponse;
import com.lab.springecommerce.dto.CartSummaryResponse;
import com.lab.springecommerce.dto.UpdateCartItemRequest;
import com.lab.springecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartSummaryResponse> getCart() {
        try {
            String customerName = getCurrentUsername();
            CartSummaryResponse cart = cartService.getCart(customerName);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<CartItemResponse> addToCart(@RequestBody AddToCartRequest request) {
        try {
            String customerName = getCurrentUsername();
            CartItemResponse cartItem = cartService.addToCart(customerName, request);
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable Long id,
                                                           @RequestBody UpdateCartItemRequest request) {
        try {
            String customerName = getCurrentUsername();
            CartItemResponse cartItem = cartService.updateCartItem(customerName, id, request);
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long id) {
        try {
            String customerName = getCurrentUsername();
            cartService.removeCartItem(customerName, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        try {
            String customerName = getCurrentUsername();
            cartService.clearCart(customerName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return authentication.getName();
    }
}