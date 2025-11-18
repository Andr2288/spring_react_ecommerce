package com.lab.springecommerce.dto;

/*
    @project   spring-ecommerce
    @class     CreateOrderResponse
    @version   1.0.0
    @since     18.11.2025 - 16:55
*/

import java.math.BigDecimal;

public class CreateOrderResponse {
    private Long orderId;
    private String message;
    private BigDecimal totalPrice;
    private String currency;
    private Integer totalItems;

    public CreateOrderResponse() {}

    public CreateOrderResponse(Long orderId, String message, BigDecimal totalPrice,
                               String currency, Integer totalItems) {
        this.orderId = orderId;
        this.message = message;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.totalItems = totalItems;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
}
