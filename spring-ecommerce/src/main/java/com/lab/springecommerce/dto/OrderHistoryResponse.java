package com.lab.springecommerce.dto;

/*
    @project   spring-ecommerce
    @class     OrderHistoryResponse
    @version   1.0.0
    @since     18.11.2025 - 20:26
*/

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderHistoryResponse {
    private Long orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private String currency;
    private Integer totalItems;
    private String status;
    private OrderDeliveryInfo deliveryInfo;
    private List<OrderItemInfo> items;

    public OrderHistoryResponse() {}

    public OrderHistoryResponse(Long orderId, LocalDateTime orderDate, BigDecimal totalPrice,
                                String currency, Integer totalItems, String status,
                                OrderDeliveryInfo deliveryInfo, List<OrderItemInfo> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
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

    public OrderDeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(OrderDeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public List<OrderItemInfo> getItems() {
        return items;
    }

    public void setItems(List<OrderItemInfo> items) {
        this.items = items;
    }

    // Nested classes for delivery and item info
    public static class OrderDeliveryInfo {
        private String deliveryName;
        private String deliveryStreet;
        private String deliveryCity;
        private String deliveryState;
        private String deliveryZip;

        public OrderDeliveryInfo() {}

        public OrderDeliveryInfo(String deliveryName, String deliveryStreet, String deliveryCity,
                                 String deliveryState, String deliveryZip) {
            this.deliveryName = deliveryName;
            this.deliveryStreet = deliveryStreet;
            this.deliveryCity = deliveryCity;
            this.deliveryState = deliveryState;
            this.deliveryZip = deliveryZip;
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
    }

    public static class OrderItemInfo {
        private Long articleId;
        private String articleName;
        private String articleDescription;
        private String imageUrl;
        private BigDecimal price;
        private String currency;
        private Integer quantity;
        private BigDecimal totalPrice;

        public OrderItemInfo() {}

        public OrderItemInfo(Long articleId, String articleName, String articleDescription,
                             String imageUrl, BigDecimal price, String currency,
                             Integer quantity, BigDecimal totalPrice) {
            this.articleId = articleId;
            this.articleName = articleName;
            this.articleDescription = articleDescription;
            this.imageUrl = imageUrl;
            this.price = price;
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