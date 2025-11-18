package com.lab.springecommerce.repository;

/*
    @project   spring-ecommerce
    @class     CartOrderRepository
    @version   1.0.0
    @since     18.11.2025 - 16:56
*/

import com.lab.springecommerce.model.CartOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.springecommerce.model.CartOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CartOrderRepository extends JpaRepository<CartOrder, Long> {

    // Знайти всі замовлення користувача з пагінацією
    @Query("SELECT co FROM CartOrder co WHERE co.customerDelivery.customer = :customer ORDER BY co.createdAt DESC")
    Page<CartOrder> findByCustomer(@Param("customer") String customer, Pageable pageable);

    // Знайти замовлення користувача з фільтром по даті
    @Query("SELECT co FROM CartOrder co WHERE co.customerDelivery.customer = :customer " +
            "AND (:startDate IS NULL OR DATE(co.createdAt) >= :startDate) " +
            "AND (:endDate IS NULL OR DATE(co.createdAt) <= :endDate) " +
            "ORDER BY co.createdAt DESC")
    Page<CartOrder> findByCustomerAndDateRange(@Param("customer") String customer,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate,
                                               Pageable pageable);

    // Знайти конкретне замовлення користувача по ID
    @Query("SELECT co FROM CartOrder co WHERE co.customerDelivery.customer = :customer " +
            "AND co.id = :orderId")
    Page<CartOrder> findByCustomerAndOrderId(@Param("customer") String customer,
                                             @Param("orderId") Long orderId,
                                             Pageable pageable);

    // Комбінований запит з усіма фільтрами
    @Query("SELECT co FROM CartOrder co WHERE co.customerDelivery.customer = :customer " +
            "AND (:startDate IS NULL OR DATE(co.createdAt) >= :startDate) " +
            "AND (:endDate IS NULL OR DATE(co.createdAt) <= :endDate) " +
            "AND (:orderId IS NULL OR co.id = :orderId) " +
            "ORDER BY co.createdAt DESC")
    Page<CartOrder> findByCustomerWithFilters(@Param("customer") String customer,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate,
                                              @Param("orderId") Long orderId,
                                              Pageable pageable);
}