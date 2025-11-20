package com.lab.springecommerce.service;

/*
    @project   spring-ecommerce
    @class     AuthService
    @version   1.0.0
    @since     15.11.2025 - 00:55
*/

import com.lab.springecommerce.dto.AuthResponse;
import com.lab.springecommerce.dto.LoginRequest;
import com.lab.springecommerce.dto.RegisterRequest;
import com.lab.springecommerce.model.Customer;
import com.lab.springecommerce.repository.CustomerRepository;
import com.lab.springecommerce.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Email pattern для валідації
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    public AuthResponse register(RegisterRequest request) {
        // Валідація запиту
        validateRegisterRequest(request);

        // Check if user already exists
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (customerRepository.existsByName(request.getName())) {
            throw new RuntimeException("Username already exists");
        }

        // Create new customer
        Customer customer = new Customer();
        customer.setName(request.getName().trim());
        customer.setEmail(request.getEmail().trim());
        customer.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        customerRepository.save(customer);

        // Generate JWT token
        String token = jwtUtil.generateToken(customer.getName());

        // Check if admin
        boolean isAdmin = "admin".equals(customer.getName());

        return new AuthResponse(token, customer.getName(), customer.getEmail(), isAdmin);
    }

    public AuthResponse login(LoginRequest request) {
        // Валідація запиту
        validateLoginRequest(request);

        // Find customer by email
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(customer.getName());

        // Check if admin
        boolean isAdmin = "admin".equals(customer.getName());

        return new AuthResponse(token, customer.getName(), customer.getEmail(), isAdmin);
    }

    // Валідація RegisterRequest
    private void validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            throw new RuntimeException("Request cannot be null");
        }

        // Валідація name
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        if (request.getName().trim().length() < 2) {
            throw new RuntimeException("Name must be at least 2 characters");
        }
        if (request.getName().trim().length() > 50) {
            throw new RuntimeException("Name must be at most 50 characters");
        }

        // Валідація email
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (request.getEmail().trim().length() > 255) {
            throw new RuntimeException("Email must be at most 255 characters");
        }
        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new RuntimeException("Email format is invalid");
        }

        // Валідація phone (опціональне)
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            if (request.getPhone().trim().length() > 255) {
                throw new RuntimeException("Phone must be at most 255 characters");
            }
            String phone = request.getPhone().trim().replaceAll("\\s+", ""); // Видаляємо пробіли
            if (!phone.matches("^\\+?[1-9]\\d{8,14}$")) {
                throw new RuntimeException("Phone format is invalid. Use format: +380501234567");
            }
        }

        // Валідація password
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }
        if (request.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }
        if (request.getPassword().length() > 100) {
            throw new RuntimeException("Password must be at most 100 characters");
        }
    }

    // Валідація LoginRequest
    private void validateLoginRequest(LoginRequest request) {
        if (request == null) {
            throw new RuntimeException("Request cannot be null");
        }

        // Валідація email
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new RuntimeException("Email format is invalid");
        }

        // Валідація password
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }
    }
}