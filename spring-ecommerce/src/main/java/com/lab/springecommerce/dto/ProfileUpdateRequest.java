package com.lab.springecommerce.dto;

/*
    @project   spring-ecommerce
    @class     ProfileUpdateRequest
    @version   1.0.0
    @since     20.11.2025 - 16:31
*/

public class ProfileUpdateRequest {

    private String name;
    private String email;
    private String phone;

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
}