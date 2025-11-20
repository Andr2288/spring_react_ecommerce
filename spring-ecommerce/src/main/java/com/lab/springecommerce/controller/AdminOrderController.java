package com.lab.springecommerce.controller;

/*
    @project   spring-ecommerce
    @class     AdminOrderController
    @version   1.0.0
    @since     19.11.2025 - 22:16
*/

import com.lab.springecommerce.dto.AdminOrderResponse;
import com.lab.springecommerce.service.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminOrderController {

    @Autowired
    private AdminOrderService adminOrderService;

    @GetMapping
    public ResponseEntity<Page<AdminOrderResponse>> getAllOrders(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long orderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        try {
            String adminUsername = getCurrentUsername();
            if (!isAdmin(adminUsername)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            Page<AdminOrderResponse> orders = adminOrderService.getAllOrders(
                    customerName, startDate, endDate, orderId, page, size, sortBy, sortDir);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<AdminOrderResponse> getOrderById(@PathVariable Long orderId) {
        try {
            String adminUsername = getCurrentUsername();
            if (!isAdmin(adminUsername)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            // Отримати конкретне замовлення (з фільтром по ID)
            Page<AdminOrderResponse> orderPage = adminOrderService.getAllOrders(
                    null, null, null, orderId, 0, 1, "createdAt", "desc");

            if (orderPage.hasContent()) {
                return ResponseEntity.ok(orderPage.getContent().get(0));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getOrderStats() {
        try {
            String adminUsername = getCurrentUsername();
            if (!isAdmin(adminUsername)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            // Повертаємо базову статистику
            // Цю функціональність можна розширити пізніше
            return ResponseEntity.ok(java.util.Map.of(
                    "message", "Order statistics endpoint available",
                    "note", "Detailed stats implementation can be added later"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Метод для отримання списку унікальних клієнтів (для dropdown фільтра)
    @GetMapping("/customers")
    public ResponseEntity<?> getUniqueCustomers() {
        try {
            String adminUsername = getCurrentUsername();
            if (!isAdmin(adminUsername)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            // Повертаємо базовий список
            // Цю функціональність можна розширити пізніше
            return ResponseEntity.ok(java.util.Map.of(
                    "message", "Customer list endpoint available",
                    "note", "Implementation can be added later to fetch unique customer names"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
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