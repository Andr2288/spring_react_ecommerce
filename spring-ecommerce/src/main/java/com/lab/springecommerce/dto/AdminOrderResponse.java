package com.lab.springecommerce.dto;

/*
    @project   spring-ecommerce
    @class     AdminOrderResponse
    @version   1.0.0
    @since     19.11.2025 - 22:14
*/

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AdminOrderResponse {
    private Long orderId;
    private LocalDateTime orderDate;
    private String customerName;
    private String customerEmail;
    private BigDecimal totalPrice;
    private String currency;
    private Integer totalItems;
    private String status;

    // Деталі доставки
    private AdminOrderDeliveryInfo deliveryInfo;

    // Список товарів у замовленні
    private List<AdminOrderItemInfo> items;

    public AdminOrderResponse() {}

    public AdminOrderResponse(Long orderId, LocalDateTime orderDate, String customerName,
                              String customerEmail, BigDecimal totalPrice, String currency,
                              Integer totalItems, String status, AdminOrderDeliveryInfo deliveryInfo,
                              List<AdminOrderItemInfo> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.totalItems = totalItems;
        this.status = status;
        this.deliveryInfo = deliveryInfo;
        this.items = items;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AdminOrderDeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(AdminOrderDeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public List<AdminOrderItemInfo> getItems() {
        return items;
    }

    public void setItems(List<AdminOrderItemInfo> items) {
        this.items = items;
    }

    // Nested class for delivery info
    public static class AdminOrderDeliveryInfo {
        private String deliveryName;
        private String deliveryStreet;
        private String deliveryCity;
        private String deliveryState;
        private String deliveryZip;
        private String ccNumberMasked; // Показуємо тільки останні 4 цифри

        public AdminOrderDeliveryInfo() {}

        public AdminOrderDeliveryInfo(String deliveryName, String deliveryStreet, String deliveryCity,
                                      String deliveryState, String deliveryZip, String ccNumberMasked) {
            this.deliveryName = deliveryName;
            this.deliveryStreet = deliveryStreet;
            this.deliveryCity = deliveryCity;
            this.deliveryState = deliveryState;
            this.deliveryZip = deliveryZip;
            this.ccNumberMasked = ccNumberMasked;
        }

        // Getters and Setters
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

        public String getCcNumberMasked() {
            return ccNumberMasked;
        }

        public void setCcNumberMasked(String ccNumberMasked) {
            this.ccNumberMasked = ccNumberMasked;
        }
    }

    // Nested class for item info
    public static class AdminOrderItemInfo {
        private Long articleId;
        private String articleName;
        private String articleDescription;
        private String articleImageUrl;
        private BigDecimal unitPrice;
        private String currency;
        private Integer quantity;
        private BigDecimal totalPrice;

        public AdminOrderItemInfo() {}

        public AdminOrderItemInfo(Long articleId, String articleName, String articleDescription,
                                  String articleImageUrl, BigDecimal unitPrice, String currency,
                                  Integer quantity, BigDecimal totalPrice) {
            this.articleId = articleId;
            this.articleName = articleName;
            this.articleDescription = articleDescription;
            this.articleImageUrl = articleImageUrl;
            this.unitPrice = unitPrice;
            this.currency = currency;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }

        // Getters and Setters
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

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
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
    }
}
