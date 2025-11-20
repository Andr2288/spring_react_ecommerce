package com.lab.springecommerce.dto;

/*
    @project   spring-ecommerce
    @class     ChangePasswordRequest
    @version   1.0.0
    @since     20.11.2025 - 16:32
*/

public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}