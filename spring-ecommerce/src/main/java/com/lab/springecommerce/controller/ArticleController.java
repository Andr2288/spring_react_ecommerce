package com.lab.springecommerce.controller;

/*
    @project   spring-ecommerce
    @class     ArticleController
    @version   1.0.0
    @since     17.11.2025 - 00:21
*/

import com.lab.springecommerce.model.Article;
import com.lab.springecommerce.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "http://localhost:5173")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public ResponseEntity<Page<Article>> getArticles(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        try {
            Page<Article> articles = articleService.getArticles(search, minPrice, maxPrice, page, size, sortBy, sortDir);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        try {
            Article article = articleService.getArticleById(id);
            return ResponseEntity.ok(article);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}