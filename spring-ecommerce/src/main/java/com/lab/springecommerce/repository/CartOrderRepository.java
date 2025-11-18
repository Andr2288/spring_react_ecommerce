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

@Repository
public interface CartOrderRepository extends JpaRepository<CartOrder, Long> {
}