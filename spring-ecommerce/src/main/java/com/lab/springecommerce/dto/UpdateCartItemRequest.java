package com.lab.springecommerce.dto;

/*
    @project   spring-ecommerce
    @class     UpdateCartItemRequest
    @version   1.0.0
    @since     17.11.2025 - 16:49
*/

public class UpdateCartItemRequest {
    private Integer quantity;

    public UpdateCartItemRequest() {}

    public UpdateCartItemRequest(Integer quantity) {
        this.quantity = quantity;
    }

    // Getters and Setters
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}