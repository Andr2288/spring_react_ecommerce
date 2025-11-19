package com.lab.springecommerce.repository;

/*
    @project   spring-ecommerce
    @class     CartOrderRepositoryExtensions
    @version   1.0.0
    @since     19.11.2025 - 22:17
*/

import com.lab.springecommerce.model.CartOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

/*
    Add these methods to CartOrderRepository interface:
*/

public interface CartOrderRepositoryExtensions {

    // ДЛЯ АДМІНА: Знайти всі замовлення з фільтрацією (без обмеження на користувача)
    @Query("SELECT co FROM CartOrder co " +
            "LEFT JOIN Customer c ON c.name = co.customerDelivery.deliveryName " +
            "WHERE (:customerName IS NULL OR LOWER(co.customerDelivery.deliveryName) LIKE LOWER(CONCAT('%', :customerName, '%'))) " +
            "AND (:startDate IS NULL OR DATE(co.createdAt) >= :startDate) " +
            "AND (:endDate IS NULL OR DATE(co.createdAt) <= :endDate) " +
            "AND (:orderId IS NULL OR co.id = :orderId)")
    Page<CartOrder> findAdminOrdersWithFilters(@Param("customerName") String customerName,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate,
                                               @Param("orderId") Long orderId,
                                               Pageable pageable);

    // ДЛЯ АДМІНА: Отримати всі замовлення без фільтрів
    @Query("SELECT co FROM CartOrder co ORDER BY co.createdAt DESC")
    Page<CartOrder> findAllOrdersAdmin(Pageable pageable);

    // ДЛЯ АДМІНА: Пошук замовлень по частині імені клієнта
    @Query("SELECT co FROM CartOrder co " +
            "WHERE LOWER(co.customerDelivery.deliveryName) LIKE LOWER(CONCAT('%', :customerName, '%')) " +
            "ORDER BY co.createdAt DESC")
    Page<CartOrder> findOrdersByCustomerNameContaining(@Param("customerName") String customerName,
                                                       Pageable pageable);

    // ДЛЯ АДМІНА: Отримати статистику замовлень
    @Query("SELECT COUNT(co) FROM CartOrder co")
    Long countAllOrders();

    @Query("SELECT SUM(co.totalPrice) FROM CartOrder co")
    java.math.BigDecimal getTotalRevenue();

    @Query("SELECT COUNT(DISTINCT co.customerDelivery.deliveryName) FROM CartOrder co")
    Long countUniqueCustomers();
}