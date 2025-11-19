package com.lab.springecommerce.service;

/*
    @project   spring-ecommerce
    @class     AdminOrderService
    @version   1.0.0
    @since     19.11.2025 - 22:15
*/

import com.lab.springecommerce.dto.AdminOrderResponse;
import com.lab.springecommerce.model.CartArticle;
import com.lab.springecommerce.model.CartOrder;
import com.lab.springecommerce.model.Customer;
import com.lab.springecommerce.repository.CartOrderRepository;
import com.lab.springecommerce.repository.CartArticleRepository;
import com.lab.springecommerce.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminOrderService {

    @Autowired
    private CartOrderRepository cartOrderRepository;

    @Autowired
    private CartArticleRepository cartArticleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Page<AdminOrderResponse> getAllOrders(String customerName, LocalDate startDate,
                                                 LocalDate endDate, Long orderId,
                                                 int page, int size, String sortBy, String sortDir) {

        // Створюємо Pageable з сортуванням
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Якщо sortBy не валідний, використовуємо за замовчуванням
        String validSortBy;
        switch (sortBy.toLowerCase()) {
            case "orderdate":
            case "createdAt":
                validSortBy = "createdAt";
                break;
            case "totalprice":
                validSortBy = "totalPrice";
                break;
            case "id":
                validSortBy = "id";
                break;
            default:
                validSortBy = "createdAt"; // За замовчуванням сортуємо по даті
                break;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, validSortBy));

        // Отримуємо замовлення з фільтрацією
        Page<CartOrder> ordersPage = cartOrderRepository.findAdminOrdersWithFilters(
                customerName, startDate, endDate, orderId, pageable);

        // Конвертуємо в AdminOrderResponse
        return ordersPage.map(this::convertToAdminOrderResponse);
    }

    private AdminOrderResponse convertToAdminOrderResponse(CartOrder cartOrder) {
        // Отримуємо деталі клієнта через delivery
        String customerName = cartOrder.getCustomerDelivery().getDeliveryName();

        // Знаходимо реального клієнта по имені (може бути неточно, але це найкраще що ми можемо зробити)
        Customer customer = customerRepository.findByName(customerName).orElse(null);
        String customerEmail = customer != null ? customer.getEmail() : "N/A";

        // Отримуємо всі товари цього замовлення
        List<CartArticle> orderItems = cartArticleRepository.findByCartOrderId(cartOrder.getId());

        // Конвертуємо товари в DTO
        List<AdminOrderResponse.AdminOrderItemInfo> items = orderItems.stream()
                .map(this::convertToAdminOrderItemInfo)
                .collect(Collectors.toList());

        // Рахуємо загальну кількість товарів
        Integer totalItems = orderItems.stream()
                .mapToInt(CartArticle::getQuantity)
                .sum();

        // Створюємо інформацію про доставку з маскуванням номера картки
        AdminOrderResponse.AdminOrderDeliveryInfo deliveryInfo =
                new AdminOrderResponse.AdminOrderDeliveryInfo(
                        cartOrder.getCustomerDelivery().getDeliveryName(),
                        cartOrder.getCustomerDelivery().getDeliveryStreet(),
                        cartOrder.getCustomerDelivery().getDeliveryCity(),
                        cartOrder.getCustomerDelivery().getDeliveryState(),
                        cartOrder.getCustomerDelivery().getDeliveryZip(),
                        maskCreditCard(cartOrder.getCustomerDelivery().getCcNumber())
                );

        return new AdminOrderResponse(
                cartOrder.getId(),
                cartOrder.getCreatedAt(),
                customerName,
                customerEmail,
                cartOrder.getTotalPrice(),
                cartOrder.getCurrency(),
                totalItems,
                "Completed", // Статичний статус, можна розширити пізніше
                deliveryInfo,
                items
        );
    }

    private AdminOrderResponse.AdminOrderItemInfo convertToAdminOrderItemInfo(CartArticle cartArticle) {
        BigDecimal totalPrice = cartArticle.getArticle().getPrice()
                .multiply(new BigDecimal(cartArticle.getQuantity()));

        return new AdminOrderResponse.AdminOrderItemInfo(
                cartArticle.getArticle().getId(),
                cartArticle.getArticle().getName(),
                cartArticle.getArticle().getDescription(),
                cartArticle.getArticle().getImageUrl(),
                cartArticle.getArticle().getPrice(),
                cartArticle.getArticle().getCurrency(),
                cartArticle.getQuantity(),
                totalPrice
        );
    }

    private String maskCreditCard(String ccNumber) {
        if (ccNumber == null || ccNumber.length() < 4) {
            return "****";
        }

        // Показуємо тільки останні 4 цифри
        String lastFour = ccNumber.substring(ccNumber.length() - 4);
        return "**** **** **** " + lastFour;
    }
}