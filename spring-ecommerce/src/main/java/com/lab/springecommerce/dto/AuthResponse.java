package com.lab.springecommerce.dto;

/*
    @project   spring-ecommerce
    @class     AuthResponse
    @version   1.0.0
    @since     15.11.2025 - 00:53
*/

public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private boolean isAdmin;

    public AuthResponse() {}

    public AuthResponse(String token, String name, String email, boolean isAdmin) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}