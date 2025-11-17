package com.lab.springecommerce.controller;

/*
    @project   spring-ecommerce
    @class     DataSeederController
    @version   1.0.0
    @since     17.11.2025
*/

import com.lab.springecommerce.service.ManualDataSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/seed")
@CrossOrigin(origins = "http://localhost:5173")
public class DataSeederController {

    @Autowired
    private ManualDataSeeder manualDataSeeder;

    @PostMapping("/run")
    public ResponseEntity<String> seedData() {
        try {
            String result = manualDataSeeder.seedData();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearData() {
        try {
            String result = manualDataSeeder.clearData();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetData() {
        try {
            String result = manualDataSeeder.resetData();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        // Простий статус без залежностей від репозиторіїв тут
        return ResponseEntity.ok("Seeder endpoints available:\n" +
                "POST /api/admin/seed/run - Create initial data\n" +
                "POST /api/admin/seed/clear - Delete all data\n" +
                "POST /api/admin/seed/reset - Clear + Seed\n" +
                "GET /api/admin/seed/status - This message");
    }
}
