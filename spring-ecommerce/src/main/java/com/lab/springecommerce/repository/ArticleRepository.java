package com.lab.springecommerce.repository;

/*
    @project   spring-ecommerce
    @class     ArticleRepository
    @version   1.0.0
    @since     17.11.2025 - 00:20
*/

import com.lab.springecommerce.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a WHERE " +
            "(:search IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:minPrice IS NULL OR a.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR a.price <= :maxPrice)")
    Page<Article> findArticlesWithFilters(
            @Param("search") String search,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );
}