package com.lab.springecommerce.repository;

/*
    @project   spring-ecommerce
    @class     CartOrderRepository
    @version   1.0.0
    @since     18.11.2025 - 16:56
*/

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

    // Комбінований запит з усіма фільтрами для користувача
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

    // ДЛЯ АДМІНА: Знайти всі замовлення з фільтрацією (без обмеження на користувача)
    @Query("SELECT co FROM CartOrder co " +
            "WHERE (:customerName IS NULL OR LOWER(co.customerDelivery.customer) LIKE LOWER(CONCAT('%', :customerName, '%'))) " +
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
            "WHERE LOWER(co.customerDelivery.customer) LIKE LOWER(CONCAT('%', :customerName, '%')) " +
            "ORDER BY co.createdAt DESC")
    Page<CartOrder> findOrdersByCustomerNameContaining(@Param("customerName") String customerName,
                                                       Pageable pageable);

    // ДЛЯ АДМІНА: Отримати статистику замовлень
    @Query("SELECT COUNT(co) FROM CartOrder co")
    Long countAllOrders();

    @Query("SELECT SUM(co.totalPrice) FROM CartOrder co")
    java.math.BigDecimal getTotalRevenue();

    @Query("SELECT COUNT(DISTINCT co.customerDelivery.customer) FROM CartOrder co")
    Long countUniqueCustomers();

    // ДЛЯ АДМІНА: Список унікальних клієнтів для dropdown фільтра
    @Query("SELECT DISTINCT co.customerDelivery.customer FROM CartOrder co ORDER BY co.customerDelivery.customer")
    java.util.List<String> findAllUniqueCustomers();

    // ДЛЯ АДМІНА: Замовлення за останні N днів
    @Query("SELECT co FROM CartOrder co WHERE co.createdAt >= :fromDate ORDER BY co.createdAt DESC")
    Page<CartOrder> findOrdersFromDate(@Param("fromDate") java.time.LocalDateTime fromDate, Pageable pageable);

    // ДЛЯ АДМІНА: Замовлення в діапазоні сум
    @Query("SELECT co FROM CartOrder co " +
            "WHERE (:minAmount IS NULL OR co.totalPrice >= :minAmount) " +
            "AND (:maxAmount IS NULL OR co.totalPrice <= :maxAmount) " +
            "ORDER BY co.createdAt DESC")
    Page<CartOrder> findOrdersByAmountRange(@Param("minAmount") java.math.BigDecimal minAmount,
                                            @Param("maxAmount") java.math.BigDecimal maxAmount,
                                            Pageable pageable);
}