package com.lab.springecommerce.repository;

/*
    @project   spring-ecommerce
    @class     CustomerDeliveryRepository
    @version   1.0.0
    @since     18.11.2025 - 16:56
*/

import com.lab.springecommerce.model.CustomerDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDeliveryRepository extends JpaRepository<CustomerDelivery, Long> {
}