package com.lab.springecommerce.service;

/*
    @project   spring-ecommerce
    @class     ManualDataSeeder
    @version   1.0.0
    @since     17.11.2025
*/

import com.lab.springecommerce.model.Article;
import com.lab.springecommerce.model.Customer;
import com.lab.springecommerce.repository.ArticleRepository;
import com.lab.springecommerce.repository.CartArticleRepository;
import com.lab.springecommerce.repository.CartOrderRepository;
import com.lab.springecommerce.repository.CustomerDeliveryRepository;
import com.lab.springecommerce.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ManualDataSeeder {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartArticleRepository cartArticleRepository;

    @Autowired
    private CartOrderRepository cartOrderRepository;

    @Autowired
    private CustomerDeliveryRepository customerDeliveryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String seedData() {
        StringBuilder result = new StringBuilder();

        try {
            // Створюємо адмін користувача
            if (!customerRepository.existsByName("admin")) {
                Customer admin = new Customer();
                admin.setName("admin");
                admin.setEmail("admin@example.com");
                admin.setPhone("+380501234567");
                admin.setPassword(passwordEncoder.encode("admin123"));
                customerRepository.save(admin);
                result.append("✅ Created admin user (admin/admin123)\n");
            } else {
                result.append("ℹ️ Admin user already exists\n");
            }

            // Створюємо тестового користувача
            if (!customerRepository.existsByEmail("test@example.com")) {
                Customer testUser = new Customer();
                testUser.setName("testuser");
                testUser.setEmail("test@example.com");
                testUser.setPhone("+380501111111");
                testUser.setPassword(passwordEncoder.encode("user123"));
                customerRepository.save(testUser);
                result.append("✅ Created test user (testuser/user123)\n");
            } else {
                result.append("ℹ️ Test user already exists\n");
            }

            // Створюємо товари
            long existingArticles = articleRepository.count();
            if (existingArticles == 0) {
                createSampleArticles();
                result.append("✅ Created 18 sample articles\n");
            } else {
                result.append("ℹ️ Articles already exist (" + existingArticles + " found)\n");
            }

            result.append("\n🎉 Data seeding completed successfully!\n");
            result.append("📊 Total customers: ").append(customerRepository.count()).append("\n");
            result.append("📦 Total articles: ").append(articleRepository.count()).append("\n");

        } catch (Exception e) {
            result.append("❌ Error during seeding: ").append(e.getMessage()).append("\n");
        }

        return result.toString();
    }

    @Transactional
    public String clearData() {
        StringBuilder result = new StringBuilder();

        try {
            // Підрахунок записів перед видаленням
            long cartArticlesCount = cartArticleRepository.count();
            long cartOrdersCount = cartOrderRepository.count();
            long customerDeliveriesCount = customerDeliveryRepository.count();
            long articlesCount = articleRepository.count();
            long customersCount = customerRepository.count();

            // ВАЖЛИВО: Видаляємо в правильному порядку через foreign key constraints

            // 1. Спочатку видаляємо CartArticle (посилається на Article та CartOrder)
            cartArticleRepository.deleteAll();
            result.append("🗑️ Deleted cart articles: ").append(cartArticlesCount).append("\n");

            // 2. Потім видаляємо CartOrder (посилається на CustomerDelivery)
            cartOrderRepository.deleteAll();
            result.append("🗑️ Deleted cart orders: ").append(cartOrdersCount).append("\n");

            // 3. Видаляємо CustomerDelivery (посилається на Customer)
            customerDeliveryRepository.deleteAll();
            result.append("🗑️ Deleted customer deliveries: ").append(customerDeliveriesCount).append("\n");

            // 4. Тепер можемо видалити Article (на них посилались CartArticle)
            articleRepository.deleteAll();
            result.append("🗑️ Deleted articles: ").append(articlesCount).append("\n");

            // 5. Наостанок видаляємо Customer (на них посилались CustomerDelivery)
            customerRepository.deleteAll();
            result.append("🗑️ Deleted customers: ").append(customersCount).append("\n");

            result.append("\n✅ All data cleared successfully!\n");

        } catch (Exception e) {
            result.append("❌ Error during clearing: ").append(e.getMessage()).append("\n");
        }

        return result.toString();
    }

    public String resetData() {
        StringBuilder result = new StringBuilder();
        result.append(clearData());
        result.append("\n");
        result.append(seedData());
        return result.toString();
    }

    private void createSampleArticles() {
        Article[] articles = {
                new Article("Red Bull Racing T-Shirt", "Official Red Bull Racing team T-shirt, size XL",
                        "https://picsum.photos/500/300?random=1",
                        new BigDecimal("29.99"), "USD", 15),

                new Article("Mercedes-AMG Petronas T-Shirt", "Official Mercedes F1 team T-shirt, size L",
                        "https://picsum.photos/500/300?random=2",
                        new BigDecimal("32.99"), "USD", 12),

                new Article("Alpine F1 Team T-Shirt", "Official Alpine F1 team T-shirt, size M",
                        "https://picsum.photos/500/300?random=3",
                        new BigDecimal("28.99"), "USD", 18),

                new Article("Scuderia Ferrari T-Shirt", "Official Ferrari F1 team T-shirt, size XL",
                        "https://picsum.photos/500/300?random=4",
                        new BigDecimal("35.99"), "USD", 20),

                new Article("Aston Martin F1 T-Shirt", "Official Aston Martin F1 team T-shirt, size XXL",
                        "https://picsum.photos/500/300?random=5",
                        new BigDecimal("34.99"), "USD", 8),

                new Article("McLaren F1 T-Shirt", "Official McLaren F1 team T-shirt, size L",
                        "https://picsum.photos/500/300?random=6",
                        new BigDecimal("31.99"), "USD", 14),

                new Article("MoneyGram Haas F1 T-Shirt", "Official Haas F1 team T-shirt, size S",
                        "https://picsum.photos/500/300?random=7",
                        new BigDecimal("26.99"), "USD", 10),

                new Article("AlphaTauri F1 T-Shirt", "Official AlphaTauri F1 team T-shirt, size M",
                        "https://picsum.photos/500/300?random=8",
                        new BigDecimal("30.99"), "USD", 16),

                new Article("Alfa Romeo F1 T-Shirt", "Official Alfa Romeo F1 team T-shirt, size M",
                        "https://picsum.photos/500/300?random=9",
                        new BigDecimal("27.99"), "USD", 13),

                new Article("Williams Racing T-Shirt", "Official Williams Racing team T-shirt, size XS",
                        "https://picsum.photos/500/300?random=10",
                        new BigDecimal("25.99"), "USD", 11),

                new Article("Formula 1 Official Cap", "Official F1 logo baseball cap, adjustable size",
                        "https://picsum.photos/500/300?random=11",
                        new BigDecimal("24.99"), "USD", 25),

                new Article("F1 Racing Hoodie", "Premium F1 racing hoodie with embroidered logo, size L",
                        "https://picsum.photos/500/300?random=12",
                        new BigDecimal("59.99"), "USD", 9),

                new Article("Pirelli Racing T-Shirt", "Official Pirelli tire sponsor T-shirt, size M",
                        "https://picsum.photos/500/300?random=13",
                        new BigDecimal("22.99"), "USD", 17),

                new Article("DHL Fastest Lap T-Shirt", "Official DHL fastest lap award T-shirt, size L",
                        "https://picsum.photos/500/300?random=14",
                        new BigDecimal("23.99"), "USD", 19),

                new Article("FIA Formula 1 Polo", "Official FIA Formula 1 polo shirt, size XL",
                        "https://picsum.photos/500/300?random=15",
                        new BigDecimal("45.99"), "USD", 7),

                new Article("F1 Race Weekend Backpack", "Official F1 race weekend backpack with laptop compartment",
                        "https://picsum.photos/500/300?random=16",
                        new BigDecimal("79.99"), "USD", 5),

                new Article("Monaco GP Limited Edition T-Shirt", "Limited edition Monaco Grand Prix T-shirt, size M",
                        "https://picsum.photos/500/300?random=17",
                        new BigDecimal("42.99"), "USD", 6),

                new Article("Silverstone GP T-Shirt", "Official Silverstone British Grand Prix T-shirt, size L",
                        "https://picsum.photos/500/300?random=18",
                        new BigDecimal("33.99"), "USD", 12)
        };

        for (Article article : articles) {
            articleRepository.save(article);
        }
    }
}