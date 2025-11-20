package com.lab.springecommerce.dto;

/*
    @project   spring-ecommerce
    @class     ProfileResponse
    @version   1.0.0
    @since     20.11.2025 - 16:32
*/

import java.time.LocalDateTime;

public class ProfileResponse {
    private String name;
    private String email;
    private String phone;
    private String imageUrl;
    private LocalDateTime createdAt;

    public ProfileResponse() {}

    public ProfileResponse(String name, String email, String phone, String imageUrl, LocalDateTime createdAt) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}