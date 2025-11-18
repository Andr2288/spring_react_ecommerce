package com.lab.springecommerce.dto;

/*
    @project   spring-ecommerce
    @class     CartSummaryResponse
    @version   1.0.0
    @since     17.11.2025 - 16:49
*/

import java.math.BigDecimal;
import java.util.List;

public class CartSummaryResponse {
    private List<CartItemResponse> items;
    private BigDecimal totalPrice;
    private String currency;
    private Integer totalItems;

    public CartSummaryResponse() {}

    public CartSummaryResponse(List<CartItemResponse> items, BigDecimal totalPrice, String currency, Integer totalItems) {
        this.items = items;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.totalItems = totalItems;
    }

    // Getters and Setters
    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
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