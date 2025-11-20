package com.lab.springecommerce.controller;

/*
    @project   spring-ecommerce
    @class     AuthController
    @version   1.0.0
    @since     15.11.2025 - 00:56
*/

import com.lab.springecommerce.dto.AuthResponse;
import com.lab.springecommerce.dto.LoginRequest;
import com.lab.springecommerce.dto.RegisterRequest;
import com.lab.springecommerce.model.Customer;
import com.lab.springecommerce.repository.CustomerRepository;
import com.lab.springecommerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Валідація буде у AuthService, а помилки - у GlobalExceptionHandler
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Валідація буде у AuthService, а помилки - у GlobalExceptionHandler
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<AuthResponse> checkAuth() {
        try {
            // Отримуємо поточну авторизацію з Security Context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).build();
            }

            String username = authentication.getName();

            // Знаходимо користувача в базі даних
            Customer customer = customerRepository.findByName(username)
                    .orElse(null);

            if (customer == null) {
                return ResponseEntity.status(401).build();
            }

            // Повертаємо дані користувача (без токена, бо він вже є)
            boolean isAdmin = "admin".equals(customer.getName());
            AuthResponse response = new AuthResponse(null, customer.getName(), customer.getEmail(), isAdmin);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}