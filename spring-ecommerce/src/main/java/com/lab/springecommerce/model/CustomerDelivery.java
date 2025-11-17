package com.lab.springecommerce.model;

/*
    @project   spring-ecommerce
    @class     CustomerDelivery
    @version   1.0.0
    @since     15.11.2025 - 00:47
*/

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Customer_Delivery")
public class CustomerDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delivery_name", length = 50, nullable = false)
    private String deliveryName;

    @Column(name = "delivery_street", length = 50, nullable = false)
    private String deliveryStreet;

    @Column(name = "delivery_city", length = 50, nullable = false)
    private String deliveryCity;

    @Column(name = "delivery_state", length = 2, nullable = false)
    private String deliveryState;

    @Column(name = "delivery_zip", length = 10, nullable = false)
    private String deliveryZip;

    @Column(name = "cc_number", length = 16, nullable = false)
    private String ccNumber;

    @Column(name = "cc_expiration", length = 5, nullable = false)
    private String ccExpiration;

    @Column(name = "cc_cvv", length = 3, nullable = false)
    private String ccCvv;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "customer", length = 50, nullable = false)
    private String customer;

    public CustomerDelivery() {
        this.createdAt = LocalDateTime.now();
    }

    public CustomerDelivery(String deliveryName, String deliveryStreet, String deliveryCity,
                            String deliveryState, String deliveryZip, String ccNumber,
                            String ccExpiration, String ccCvv, String customer) {
        this();
        this.deliveryName = deliveryName;
        this.deliveryStreet = deliveryStreet;
        this.deliveryCity = deliveryCity;
        this.deliveryState = deliveryState;
        this.deliveryZip = deliveryZip;
        this.ccNumber = ccNumber;
        this.ccExpiration = ccExpiration;
        this.ccCvv = ccCvv;
        this.customer = customer;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryStreet() {
        return deliveryStreet;
    }

    public void setDeliveryStreet(String deliveryStreet) {
        this.deliveryStreet = deliveryStreet;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getDeliveryZip() {
        return deliveryZip;
    }

    public void setDeliveryZip(String deliveryZip) {
        this.deliveryZip = deliveryZip;
    }

    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getCcExpiration() {
        return ccExpiration;
    }

    public void setCcExpiration(String ccExpiration) {
        this.ccExpiration = ccExpiration;
    }

    public String getCcCvv() {
        return ccCvv;
    }

    public void setCcCvv(String ccCvv) {
        this.ccCvv = ccCvv;
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
}