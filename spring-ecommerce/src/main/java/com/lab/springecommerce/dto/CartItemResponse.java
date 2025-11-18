package com.lab.springecommerce.dto;

/*
    @project   spring-ecommerce
    @class     CartItemResponse
    @version   1.0.0
    @since     17.11.2025 - 16:49
*/

import java.math.BigDecimal;

public class CartItemResponse {
    private Long id;
    private Long articleId;
    private String articleName;
    private String articleDescription;
    private String articleImageUrl;
    private BigDecimal articlePrice;
    private String currency;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Integer availableQuantity;

    public CartItemResponse() {}

    public CartItemResponse(Long id, Long articleId, String articleName, String articleDescription,
                            String articleImageUrl, BigDecimal articlePrice, String currency,
                            Integer quantity, BigDecimal totalPrice, Integer availableQuantity) {
        this.id = id;
        this.articleId = articleId;
        this.articleName = articleName;
        this.articleDescription = articleDescription;
        this.articleImageUrl = articleImageUrl;
        this.articlePrice = articlePrice;
        this.currency = currency;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.availableQuantity = availableQuantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getArticleImageUrl() {
        return articleImageUrl;
    }

    public void setArticleImageUrl(String articleImageUrl) {
        this.articleImageUrl = articleImageUrl;
    }

    public BigDecimal getArticlePrice() {
        return articlePrice;
    }

    public void setArticlePrice(BigDecimal articlePrice) {
        this.articlePrice = articlePrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}
