package com.lab.springecommerce.model;

/*
    @project   spring-ecommerce
    @class     CartArticle
    @version   1.0.0
    @since     15.11.2025 - 00:46
*/

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Cart_Article")
public class CartArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "customer", length = 50, nullable = false)
    private String customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article", nullable = false)
    private Article article;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_order")
    private CartOrder cartOrder;

    public CartArticle() {
        this.createdAt = LocalDateTime.now();
    }

    public CartArticle(String customer, Article article, Integer quantity) {
        this();
        this.customer = customer;
        this.article = article;
        this.quantity = quantity;
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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CartOrder getCartOrder() {
        return cartOrder;
    }

    public void setCartOrder(CartOrder cartOrder) {
        this.cartOrder = cartOrder;
    }
}
