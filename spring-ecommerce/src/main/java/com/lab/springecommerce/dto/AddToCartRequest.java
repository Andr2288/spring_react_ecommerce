package com.lab.springecommerce.dto;

/*
    @project   spring-ecommerce
    @class     AddToCartRequest
    @version   1.0.0
    @since     17.11.2025 - 16:48
*/

public class AddToCartRequest {
    private Long articleId;
    private Integer quantity;

    public AddToCartRequest() {}

    public AddToCartRequest(Long articleId, Integer quantity) {
        this.articleId = articleId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}