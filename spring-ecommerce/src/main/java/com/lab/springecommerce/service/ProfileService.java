package com.lab.springecommerce.service;

/*
    @project   spring-ecommerce
    @class     ProfileService
    @version   1.0.0
    @since     20.11.2025 - 16:33
*/

import com.lab.springecommerce.dto.ChangePasswordRequest;
import com.lab.springecommerce.dto.ProfileResponse;
import com.lab.springecommerce.dto.ProfileUpdateRequest;
import com.lab.springecommerce.model.Customer;
import com.lab.springecommerce.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class ProfileService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String uploadDir = "uploads/avatars/";

    // Email pattern для валідації
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    public ProfileResponse getProfile(String customerName) {
        Customer customer = customerRepository.findById(customerName)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return new ProfileResponse(
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getImageUrl(),
                customer.getCreatedAt()
        );
    }

    @Transactional
    public ProfileResponse updateProfile(String customerName, ProfileUpdateRequest request) {
        Customer customer = customerRepository.findById(customerName)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Валідація запиту
        validateProfileUpdateRequest(request);

        // Перевіряємо унікальність email (якщо змінюється)
        if (!customer.getEmail().equals(request.getEmail())) {
            if (customerRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
        }

        // Оновлюємо дані
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        // Ім'я не можна змінювати, бо це Primary Key
        // customer.setName(request.getName());

        customerRepository.save(customer);

        return new ProfileResponse(
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getImageUrl(),
                customer.getCreatedAt()
        );
    }

    @Transactional
    public void changePassword(String customerName, ChangePasswordRequest request) {
        Customer customer = customerRepository.findById(customerName)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Валідація запиту
        validateChangePasswordRequest(request);

        // Перевіряємо поточний пароль
        if (!passwordEncoder.matches(request.getCurrentPassword(), customer.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Встановлюємо новий пароль
        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerRepository.save(customer);
    }

    @Transactional
    public String uploadAvatar(String customerName, MultipartFile file) throws IOException {
        Customer customer = customerRepository.findById(customerName)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Перевіряємо тип файлу
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Only image files are allowed");
        }

        // Створюємо директорію, якщо не існує
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Генеруємо унікальне ім'я файлу
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = customerName + "_" + UUID.randomUUID().toString() + extension;

        // Зберігаємо файл
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Оновлюємо URL аватара в БД
        String imageUrl = "/uploads/avatars/" + filename;
        customer.setImageUrl(imageUrl);
        customerRepository.save(customer);

        return imageUrl;
    }

    // Валідація ProfileUpdateRequest
    private void validateProfileUpdateRequest(ProfileUpdateRequest request) {
        if (request == null) {
            throw new RuntimeException("Request cannot be null");
        }

        // Валідація name
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        if (request.getName().trim().length() > 50) {
            throw new RuntimeException("Name must be at most 50 characters");
        }

        // Валідація email
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (request.getEmail().trim().length() > 255) {
            throw new RuntimeException("Email must be at most 255 characters");
        }
        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new RuntimeException("Email format is invalid");
        }

        // Валідація phone (опціональне)
        if (request.getPhone() != null && request.getPhone().trim().length() > 255) {
            throw new RuntimeException("Phone must be at most 255 characters");
        }

        // Валідація формату телефону (якщо заповнене)
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            String phone = request.getPhone().trim().replaceAll("\\s+", ""); // Видаляємо пробіли
            if (!phone.matches("^\\+?[1-9]\\d{8,14}$")) {
                throw new RuntimeException("Phone format is invalid. Use format: +380501234567");
            }
        }
    }

    // Валідація ChangePasswordRequest
    private void validateChangePasswordRequest(ChangePasswordRequest request) {
        if (request == null) {
            throw new RuntimeException("Request cannot be null");
        }

        // Валідація currentPassword
        if (request.getCurrentPassword() == null || request.getCurrentPassword().trim().isEmpty()) {
            throw new RuntimeException("Current password is required");
        }

        // Валідація newPassword
        if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
            throw new RuntimeException("New password is required");
        }
        if (request.getNewPassword().length() < 6) {
            throw new RuntimeException("New password must be at least 6 characters");
        }
    }
}