package com.lab.springecommerce.service;

/*
    @project   spring-ecommerce
    @class     ArticleService
    @version   1.0.0
    @since     17.11.2025 - 00:20
*/

import com.lab.springecommerce.model.Article;
import com.lab.springecommerce.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Page<Article> getArticles(String search, BigDecimal minPrice, BigDecimal maxPrice,
                                     int page, int size, String sortBy, String sortDir) {

        // Валідація параметрів
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 12;

        // Створення Sort об'єкта
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return articleRepository.findArticlesWithFilters(search, minPrice, maxPrice, pageable);
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
    }
}