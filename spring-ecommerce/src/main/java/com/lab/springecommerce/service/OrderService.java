package com.lab.springecommerce.service;

/*
    @project   spring-ecommerce
    @class     OrderService
    @version   1.0.0
    @since     18.11.2025 - 16:57
*/

import com.lab.springecommerce.dto.CreateOrderRequest;
import com.lab.springecommerce.dto.CreateOrderResponse;
import com.lab.springecommerce.model.Article;
import com.lab.springecommerce.model.CartArticle;
import com.lab.springecommerce.model.CartOrder;
import com.lab.springecommerce.model.CustomerDelivery;
import com.lab.springecommerce.repository.ArticleRepository;
import com.lab.springecommerce.repository.CartArticleRepository;
import com.lab.springecommerce.repository.CartOrderRepository;
import com.lab.springecommerce.repository.CustomerDeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class OrderService {

    @Autowired
    private CartArticleRepository cartArticleRepository;

    @Autowired
    private CustomerDeliveryRepository customerDeliveryRepository;

    @Autowired
    private CartOrderRepository cartOrderRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Transactional
    public CreateOrderResponse createOrder(String customerName, CreateOrderRequest request) {
        // 1. Валідація даних
        validateOrderData(request);

        // 2. Отримуємо товари з кошика
        List<CartArticle> cartItems = cartArticleRepository.findByCustomerAndCartOrderIsNull(customerName);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 3. Перевіряємо наявність товарів і рахуємо загальну суму
        BigDecimal totalPrice = BigDecimal.ZERO;
        String currency = cartItems.get(0).getArticle().getCurrency();

        for (CartArticle cartItem : cartItems) {
            Article article = cartItem.getArticle();

            if (article.getAvailableQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + article.getName() +
                        ". Available: " + article.getAvailableQuantity() +
                        ", requested: " + cartItem.getQuantity());
            }

            BigDecimal itemTotal = article.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
        }

        // 4. Створюємо CustomerDelivery
        CustomerDelivery customerDelivery = new CustomerDelivery(
                request.getDeliveryName(),
                request.getDeliveryStreet(),
                request.getDeliveryCity(),
                request.getDeliveryState(),
                request.getDeliveryZip(),
                request.getCcNumber(),
                request.getCcExpiration(),
                request.getCcCvv(),
                customerName
        );
        customerDelivery = customerDeliveryRepository.save(customerDelivery);

        // 5. Створюємо CartOrder
        CartOrder cartOrder = new CartOrder(totalPrice, currency, customerDelivery);
        cartOrder = cartOrderRepository.save(cartOrder);

        // 6. Оновлюємо CartArticle записи (прив'язуємо до замовлення)
        for (CartArticle cartItem : cartItems) {
            cartItem.setCartOrder(cartOrder);
            cartArticleRepository.save(cartItem);
        }

        // 7. Зменшуємо available_quantity для кожного товару
        for (CartArticle cartItem : cartItems) {
            Article article = cartItem.getArticle();
            article.setAvailableQuantity(article.getAvailableQuantity() - cartItem.getQuantity());
            articleRepository.save(article);
        }

        // 8. Повертаємо успішну відповідь
        return new CreateOrderResponse(
                cartOrder.getId(),
                "Order placed successfully",
                totalPrice,
                currency,
                cartItems.stream().mapToInt(CartArticle::getQuantity).sum()
        );
    }

    private void validateOrderData(CreateOrderRequest request) {
        // Валідація даних доставки
        if (isNullOrEmpty(request.getDeliveryName())) {
            throw new RuntimeException("Delivery name is required");
        }
        if (isNullOrEmpty(request.getDeliveryStreet())) {
            throw new RuntimeException("Delivery street is required");
        }
        if (isNullOrEmpty(request.getDeliveryCity())) {
            throw new RuntimeException("Delivery city is required");
        }
        if (isNullOrEmpty(request.getDeliveryState())) {
            throw new RuntimeException("Delivery state is required");
        }
        if (request.getDeliveryState().length() != 2) {
            throw new RuntimeException("Delivery state must be 2 characters (e.g., CA, NY)");
        }
        if (isNullOrEmpty(request.getDeliveryZip())) {
            throw new RuntimeException("Delivery ZIP is required");
        }
        if (request.getDeliveryZip().length() < 5 || request.getDeliveryZip().length() > 10) {
            throw new RuntimeException("Invalid ZIP code format");
        }

        // Валідація даних картки
        if (isNullOrEmpty(request.getCcNumber())) {
            throw new RuntimeException("Credit card number is required");
        }
        // Прибираємо пробіли та дефіси для валідації
        String ccNumber = request.getCcNumber().replaceAll("[\\s-]", "");
        if (ccNumber.length() != 16 || !ccNumber.matches("\\d{16}")) {
            throw new RuntimeException("Credit card number must be 16 digits");
        }

        if (isNullOrEmpty(request.getCcExpiration())) {
            throw new RuntimeException("Credit card expiration is required");
        }
        if (!Pattern.matches("^(0[1-9]|1[0-2])/\\d{2}$", request.getCcExpiration())) {
            throw new RuntimeException("Credit card expiration must be in MM/YY format");
        }

        if (isNullOrEmpty(request.getCcCvv())) {
            throw new RuntimeException("Credit card CVV is required");
        }
        if (request.getCcCvv().length() != 3 || !request.getCcCvv().matches("\\d{3}")) {
            throw new RuntimeException("CVV must be 3 digits");
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}