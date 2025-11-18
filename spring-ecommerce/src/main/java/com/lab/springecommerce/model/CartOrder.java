package com.lab.springecommerce.model;

/*
    @project   spring-ecommerce
    @class     CartOrder
    @version   1.0.0
    @since     18.11.2025 - 17:02
*/

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Cart_Order")
public class CartOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_delivery", nullable = false)
    private CustomerDelivery customerDelivery;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public CartOrder() {
        this.createdAt = LocalDateTime.now();
    }

    public CartOrder(BigDecimal totalPrice, String currency, CustomerDelivery customerDelivery) {
        this();
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.customerDelivery = customerDelivery;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public CustomerDelivery getCustomerDelivery() {
        return customerDelivery;
    }

    public void setCustomerDelivery(CustomerDelivery customerDelivery) {
        this.customerDelivery = customerDelivery;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}