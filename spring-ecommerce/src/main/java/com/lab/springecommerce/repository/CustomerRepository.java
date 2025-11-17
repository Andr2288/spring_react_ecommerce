package com.lab.springecommerce.repository;

/*
    @project   spring-ecommerce
    @class     CustomerRepository
    @version   1.0.0
    @since     15.11.2025 - 00:49
*/

import com.lab.springecommerce.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByName(String name);

    boolean existsByEmail(String email);

    boolean existsByName(String name);
}