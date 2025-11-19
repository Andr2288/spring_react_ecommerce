package com.lab.springecommerce.dto;

/*
    @project   spring-ecommerce
    @class     CreateArticleRequest
    @version   1.0.0
    @since     18.11.2025 - 20:48
*/

import java.math.BigDecimal;

public class CreateArticleRequest {
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private String currency;
    private Integer availableQuantity;

    public CreateArticleRequest() {}

    public CreateArticleRequest(String name, String description, String imageUrl,
                                BigDecimal price, String currency, Integer availableQuantity) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.currency = currency;
        this.availableQuantity = availableQuantity;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}