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

@Service
public class AuthService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (customerRepository.existsByName(request.getName())) {
            throw new RuntimeException("Username already exists");
        }

        // Create new customer
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        customerRepository.save(customer);

        // Generate JWT token
        String token = jwtUtil.generateToken(customer.getName());

        // Check if admin
        boolean isAdmin = "admin".equals(customer.getName());

        return new AuthResponse(token, customer.getName(), customer.getEmail(), isAdmin);
    }

    public AuthResponse login(LoginRequest request) {
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
}