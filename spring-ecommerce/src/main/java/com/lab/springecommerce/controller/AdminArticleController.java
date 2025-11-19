package com.lab.springecommerce.controller;

/*
    @project   spring-ecommerce
    @class     AdminArticleController
    @version   1.0.0
    @since     18.11.2025 - 20:48
*/

import com.lab.springecommerce.dto.CreateArticleRequest;
import com.lab.springecommerce.dto.UpdateArticleRequest;
import com.lab.springecommerce.model.Article;
import com.lab.springecommerce.service.AdminArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/admin/articles")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminArticleController {

    @Autowired
    private AdminArticleService adminArticleService;

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
            String adminUsername = getCurrentUsername();
            if (!isAdmin(adminUsername)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            Page<Article> articles = adminArticleService.getArticles(search, minPrice, maxPrice, page, size, sortBy, sortDir);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody CreateArticleRequest request) {
        try {
            String adminUsername = getCurrentUsername();
            if (!isAdmin(adminUsername)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            Article article = adminArticleService.createArticle(request);
            return ResponseEntity.ok(article);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
        try {
            String adminUsername = getCurrentUsername();
            if (!isAdmin(adminUsername)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            Article article = adminArticleService.updateArticle(id, request);
            return ResponseEntity.ok(article);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        try {
            String adminUsername = getCurrentUsername();
            if (!isAdmin(adminUsername)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            adminArticleService.deleteArticle(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        try {
            String adminUsername = getCurrentUsername();
            if (!isAdmin(adminUsername)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            Article article = adminArticleService.getArticleById(id);
            return ResponseEntity.ok(article);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return authentication.getName();
    }

    private boolean isAdmin(String username) {
        return "admin".equals(username);
    }
}
