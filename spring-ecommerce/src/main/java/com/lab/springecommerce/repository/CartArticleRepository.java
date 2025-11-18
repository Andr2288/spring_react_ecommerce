package com.lab.springecommerce.repository;

/*
    @project   spring-ecommerce
    @class     CartArticleRepository
    @version   1.0.0
    @since     17.11.2025 - 16:48
*/

import com.lab.springecommerce.model.CartArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartArticleRepository extends JpaRepository<CartArticle, Long> {

    // Знайти всі товари в кошику користувача (ще не оформлені в замовлення)
    @Query("SELECT ca FROM CartArticle ca WHERE ca.customer = :customer AND ca.cartOrder IS NULL")
    List<CartArticle> findByCustomerAndCartOrderIsNull(@Param("customer") String customer);

    // Знайти конкретний товар в кошику користувача
    @Query("SELECT ca FROM CartArticle ca WHERE ca.customer = :customer AND ca.article.id = :articleId AND ca.cartOrder IS NULL")
    CartArticle findByCustomerAndArticleIdAndCartOrderIsNull(@Param("customer") String customer, @Param("articleId") Long articleId);

    // Видалити всі товари з кошика користувача
    void deleteByCustomerAndCartOrderIsNull(String customer);
}