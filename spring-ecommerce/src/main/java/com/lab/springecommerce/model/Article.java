package com.lab.springecommerce.model;

/*
    @project   spring-ecommerce
    @class     Article
    @version   1.0.0
    @since     15.11.2025 - 00:45
*/

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    public Article() {
        this.createdAt = LocalDateTime.now();
    }

    public Article(String name, String description, String imageUrl, BigDecimal price, String currency, Integer availableQuantity) {
        this();
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.currency = currency;
        this.availableQuantity = availableQuantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

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