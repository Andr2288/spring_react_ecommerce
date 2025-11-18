package com.lab.springecommerce.service;

/*
    @project   spring-ecommerce
    @class     CartService
    @version   1.0.0
    @since     17.11.2025 - 16:49
*/

import com.lab.springecommerce.dto.AddToCartRequest;
import com.lab.springecommerce.dto.CartItemResponse;
import com.lab.springecommerce.dto.CartSummaryResponse;
import com.lab.springecommerce.dto.UpdateCartItemRequest;
import com.lab.springecommerce.model.Article;
import com.lab.springecommerce.model.CartArticle;
import com.lab.springecommerce.repository.ArticleRepository;
import com.lab.springecommerce.repository.CartArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartArticleRepository cartArticleRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public CartSummaryResponse getCart(String customerName) {
        List<CartArticle> cartItems = cartArticleRepository.findByCustomerAndCartOrderIsNull(customerName);

        List<CartItemResponse> items = cartItems.stream()
                .map(this::convertToCartItemResponse)
                .collect(Collectors.toList());

        BigDecimal totalPrice = items.stream()
                .map(CartItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItems = items.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();

        String currency = items.isEmpty() ? "USD" : items.get(0).getCurrency();

        return new CartSummaryResponse(items, totalPrice, currency, totalItems);
    }

    @Transactional
    public CartItemResponse addToCart(String customerName, AddToCartRequest request) {
        // Валідація
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        // Знайти товар
        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new RuntimeException("Article not found"));

        // Перевірити доступність товару
        if (article.getAvailableQuantity() < request.getQuantity()) {
            throw new RuntimeException("Not enough items in stock. Available: " + article.getAvailableQuantity());
        }

        // Перевірити чи товар вже є в кошику
        CartArticle existingCartItem = cartArticleRepository
                .findByCustomerAndArticleIdAndCartOrderIsNull(customerName, request.getArticleId());

        if (existingCartItem != null) {
            // Оновити кількість існуючого товару
            int newQuantity = existingCartItem.getQuantity() + request.getQuantity();

            if (newQuantity > article.getAvailableQuantity()) {
                throw new RuntimeException("Not enough items in stock. Available: " + article.getAvailableQuantity() +
                        ", in cart: " + existingCartItem.getQuantity());
            }

            existingCartItem.setQuantity(newQuantity);
            CartArticle savedItem = cartArticleRepository.save(existingCartItem);
            return convertToCartItemResponse(savedItem);
        } else {
            // Створити новий запис в кошику
            CartArticle cartArticle = new CartArticle(customerName, article, request.getQuantity());
            CartArticle savedItem = cartArticleRepository.save(cartArticle);
            return convertToCartItemResponse(savedItem);
        }
    }

    @Transactional
    public CartItemResponse updateCartItem(String customerName, Long cartItemId, UpdateCartItemRequest request) {
        // Валідація
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        // Знайти товар в кошику
        CartArticle cartItem = cartArticleRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Перевірити що це товар поточного користувача
        if (!cartItem.getCustomer().equals(customerName)) {
            throw new RuntimeException("Access denied");
        }

        // Перевірити що товар ще не оформлений в замовлення
        if (cartItem.getCartOrder() != null) {
            throw new RuntimeException("Cannot update ordered item");
        }

        // Перевірити доступність товару
        if (cartItem.getArticle().getAvailableQuantity() < request.getQuantity()) {
            throw new RuntimeException("Not enough items in stock. Available: " +
                    cartItem.getArticle().getAvailableQuantity());
        }

        // Оновити кількість
        cartItem.setQuantity(request.getQuantity());
        CartArticle savedItem = cartArticleRepository.save(cartItem);
        return convertToCartItemResponse(savedItem);
    }

    @Transactional
    public void removeCartItem(String customerName, Long cartItemId) {
        // Знайти товар в кошику
        CartArticle cartItem = cartArticleRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Перевірити що це товар поточного користувача
        if (!cartItem.getCustomer().equals(customerName)) {
            throw new RuntimeException("Access denied");
        }

        // Перевірити що товар ще не оформлений в замовлення
        if (cartItem.getCartOrder() != null) {
            throw new RuntimeException("Cannot remove ordered item");
        }

        cartArticleRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(String customerName) {
        cartArticleRepository.deleteByCustomerAndCartOrderIsNull(customerName);
    }

    private CartItemResponse convertToCartItemResponse(CartArticle cartArticle) {
        Article article = cartArticle.getArticle();
        BigDecimal totalPrice = article.getPrice().multiply(new BigDecimal(cartArticle.getQuantity()));

        return new CartItemResponse(
                cartArticle.getId(),
                article.getId(),
                article.getName(),
                article.getDescription(),
                article.getImageUrl(),
                article.getPrice(),
                article.getCurrency(),
                cartArticle.getQuantity(),
                totalPrice,
                article.getAvailableQuantity()
        );
    }
}