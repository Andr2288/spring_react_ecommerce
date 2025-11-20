package com.lab.springecommerce.controller;

/*
    @project   spring-ecommerce
    @class     ProfileController
    @version   1.0.0
    @since     20.11.2025 - 16:33
*/

import com.lab.springecommerce.dto.ChangePasswordRequest;
import com.lab.springecommerce.dto.ProfileResponse;
import com.lab.springecommerce.dto.ProfileUpdateRequest;
import com.lab.springecommerce.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(Authentication authentication) {
        String customerName = authentication.getName();
        ProfileResponse profile = profileService.getProfile(customerName);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            Authentication authentication,
            @RequestBody ProfileUpdateRequest request) {

        String customerName = authentication.getName();
        ProfileResponse updatedProfile = profileService.updateProfile(customerName, request);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request) {

        String customerName = authentication.getName();
        profileService.changePassword(customerName, request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadAvatar(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) {

        try {
            String customerName = authentication.getName();
            String imageUrl = profileService.uploadAvatar(customerName, file);

            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", imageUrl);
            response.put("message", "Avatar uploaded successfully");
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to upload file: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}