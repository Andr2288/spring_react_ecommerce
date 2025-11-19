package com.lab.springecommerce.service;

/*
    @project   spring-ecommerce
    @class     AdminArticleService
    @version   1.0.0
    @since     18.11.2025 - 20:49
*/

import com.lab.springecommerce.dto.CreateArticleRequest;
import com.lab.springecommerce.dto.UpdateArticleRequest;
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
public class AdminArticleService {

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

    public Article createArticle(CreateArticleRequest request) {
        // Валідація
        validateArticleData(request.getName(), request.getDescription(), request.getPrice(),
                request.getCurrency(), request.getAvailableQuantity(), request.getImageUrl());

        // Створення нового товару
        Article article = new Article(
                request.getName(),
                request.getDescription(),
                request.getImageUrl(),
                request.getPrice(),
                request.getCurrency(),
                request.getAvailableQuantity()
        );

        return articleRepository.save(article);
    }

    public Article updateArticle(Long id, UpdateArticleRequest request) {
        // Знаходимо товар
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));

        // Валідація
        validateArticleData(request.getName(), request.getDescription(), request.getPrice(),
                request.getCurrency(), request.getAvailableQuantity(), request.getImageUrl());

        // Оновлення полів
        article.setName(request.getName());
        article.setDescription(request.getDescription());
        article.setImageUrl(request.getImageUrl());
        article.setPrice(request.getPrice());
        article.setCurrency(request.getCurrency());
        article.setAvailableQuantity(request.getAvailableQuantity());

        return articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        // Перевіряємо що товар існує
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));

        // TODO: Можна додати перевірку чи товар не використовується в активних замовленнях
        // Поки що просто видаляємо
        articleRepository.delete(article);
    }

    private void validateArticleData(String name, String description, BigDecimal price,
                                     String currency, Integer availableQuantity, String imageUrl) {

        // Валідація назви
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Product name is required");
        }
        if (name.trim().length() > 50) {
            throw new RuntimeException("Product name must be 50 characters or less");
        }

        // Валідація опису
        if (description == null || description.trim().isEmpty()) {
            throw new RuntimeException("Product description is required");
        }
        if (description.trim().length() > 255) {
            throw new RuntimeException("Product description must be 255 characters or less");
        }

        // Валідація ціни
        if (price == null) {
            throw new RuntimeException("Product price is required");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Product price must be greater than 0");
        }
        if (price.compareTo(new BigDecimal("999999.99")) > 0) {
            throw new RuntimeException("Product price must be less than 999999.99");
        }

        // Валідація валюти
        if (currency == null || currency.trim().isEmpty()) {
            throw new RuntimeException("Product currency is required");
        }
        if (!currency.matches("^[A-Z]{3}$")) {
            throw new RuntimeException("Currency must be a 3-letter code (e.g., USD, EUR)");
        }

        // Валідація кількості
        if (availableQuantity == null) {
            throw new RuntimeException("Available quantity is required");
        }
        if (availableQuantity < 0) {
            throw new RuntimeException("Available quantity must be 0 or greater");
        }
        if (availableQuantity > 999999) {
            throw new RuntimeException("Available quantity must be less than 999999");
        }

        // Валідація URL зображення (опціональна)
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            if (imageUrl.trim().length() > 255) {
                throw new RuntimeException("Image URL must be 255 characters or less");
            }
            // Простий regex для валідації URL
            if (!imageUrl.matches("^https?://.+$") && !imageUrl.startsWith("data:image/")) {
                throw new RuntimeException("Image URL must be a valid HTTP(S) URL or data URL");
            }
        }
    }
}