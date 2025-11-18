package com.lab.springecommerce.service;

/*
    @project   spring-ecommerce
    @class     OrderService
    @version   1.0.0
    @since     18.11.2025 - 16:57
*/

import com.lab.springecommerce.dto.CreateOrderRequest;
import com.lab.springecommerce.dto.CreateOrderResponse;
import com.lab.springecommerce.dto.OrderHistoryResponse;
import com.lab.springecommerce.model.Article;
import com.lab.springecommerce.model.CartArticle;
import com.lab.springecommerce.model.CartOrder;
import com.lab.springecommerce.model.CustomerDelivery;
import com.lab.springecommerce.repository.ArticleRepository;
import com.lab.springecommerce.repository.CartArticleRepository;
import com.lab.springecommerce.repository.CartOrderRepository;
import com.lab.springecommerce.repository.CustomerDeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
                cartItems.size()
        );
    }

    public Page<OrderHistoryResponse> getMyOrders(String customerName, int page, int size,
                                                  LocalDate startDate, LocalDate endDate, Long orderId) {

        Pageable pageable = PageRequest.of(page, size);

        // Отримуємо замовлення з фільтрацією
        Page<CartOrder> orders = cartOrderRepository.findByCustomerWithFilters(
                customerName, startDate, endDate, orderId, pageable);

        // Перетворюємо CartOrder в OrderHistoryResponse
        return orders.map(order -> {
            // Отримуємо товари замовлення
            List<CartArticle> orderItems = cartArticleRepository.findByCartOrderId(order.getId());

            // Конвертуємо товари в OrderItemInfo
            List<OrderHistoryResponse.OrderItemInfo> items = orderItems.stream()
                    .map(cartArticle -> {
                        Article article = cartArticle.getArticle();
                        BigDecimal itemTotal = article.getPrice().multiply(new BigDecimal(cartArticle.getQuantity()));

                        return new OrderHistoryResponse.OrderItemInfo(
                                article.getId(),
                                article.getName(),
                                article.getDescription(),
                                article.getImageUrl(),
                                article.getPrice(),
                                article.getCurrency(),
                                cartArticle.getQuantity(),
                                itemTotal
                        );
                    })
                    .collect(Collectors.toList());

            // Конвертуємо дані доставки
            CustomerDelivery delivery = order.getCustomerDelivery();
            OrderHistoryResponse.OrderDeliveryInfo deliveryInfo = new OrderHistoryResponse.OrderDeliveryInfo(
                    delivery.getDeliveryName(),
                    delivery.getDeliveryStreet(),
                    delivery.getDeliveryCity(),
                    delivery.getDeliveryState(),
                    delivery.getDeliveryZip()
            );

            // Рахуємо загальну кількість товарів
            int totalItems = orderItems.stream()
                    .mapToInt(CartArticle::getQuantity)
                    .sum();

            return new OrderHistoryResponse(
                    order.getId(),
                    order.getCreatedAt(),
                    order.getTotalPrice(),
                    order.getCurrency(),
                    totalItems,
                    "Completed", // статус завжди Completed для цього проекту
                    deliveryInfo,
                    items
            );
        });
    }

    private void validateOrderData(CreateOrderRequest request) {
        // Валідація даних доставки
        if (request.getDeliveryName() == null || request.getDeliveryName().trim().isEmpty()) {
            throw new RuntimeException("Delivery name is required");
        }
        if (request.getDeliveryStreet() == null || request.getDeliveryStreet().trim().isEmpty()) {
            throw new RuntimeException("Delivery street is required");
        }
        if (request.getDeliveryCity() == null || request.getDeliveryCity().trim().isEmpty()) {
            throw new RuntimeException("Delivery city is required");
        }
        if (request.getDeliveryState() == null || request.getDeliveryState().trim().isEmpty()) {
            throw new RuntimeException("Delivery state is required");
        }
        if (request.getDeliveryZip() == null || request.getDeliveryZip().trim().isEmpty()) {
            throw new RuntimeException("Delivery zip is required");
        }

        // Валідація даних картки
        validateCreditCard(request.getCcNumber(), request.getCcExpiration(), request.getCcCvv());
    }

    private void validateCreditCard(String ccNumber, String ccExpiration, String ccCvv) {
        // Валідація номеру картки (16 цифр)
        if (ccNumber == null || !Pattern.matches("\\d{16}", ccNumber.replaceAll("\\s+", ""))) {
            throw new RuntimeException("Credit card number must be 16 digits");
        }

        // Валідація терміну дії (MM/YY)
        if (ccExpiration == null || !Pattern.matches("\\d{2}/\\d{2}", ccExpiration)) {
            throw new RuntimeException("Credit card expiration must be in MM/YY format");
        }

        // Валідація CVV (3 цифри)
        if (ccCvv == null || !Pattern.matches("\\d{3}", ccCvv)) {
            throw new RuntimeException("CVV must be 3 digits");
        }
    }
}