package com.lab.springecommerce.config;

/*
    @project   spring-ecommerce
    @class     SecurityConfig
    @version   1.0.1
    @since     19.11.2025 - Fixed seed endpoints order
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // ПРИБИРАЄМО всі форми авторизації що показують popup
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        // PUBLIC - реєстрація та логін доступні всім
                        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                        // PROTECTED - checkAuth потребує JWT токена
                        .requestMatchers(HttpMethod.GET, "/api/auth/check").authenticated()
                        // PUBLIC - перегляд товарів доступний всім
                        .requestMatchers(HttpMethod.GET, "/api/articles/**").permitAll()
                        // PUBLIC - seed endpoints (тільки для розробки!) - МАЄ БУТИ ПЕРЕД /api/admin/**
                        .requestMatchers("/api/admin/seed/**").permitAll()
                        // ADMIN ONLY - admin endpoints потребують авторизації (перевірка ролі в контролері)
                        .requestMatchers("/api/admin/**").authenticated()
                        // PROTECTED - cart endpoints потребують авторизації
                        .requestMatchers("/api/cart/**").authenticated()
                        // PROTECTED - orders endpoints потребують авторизації
                        .requestMatchers("/api/orders/**").authenticated()
                        // Усі інші запити потребують JWT токена
                        .anyRequest().authenticated()
                )
                // Додаємо JWT фільтр
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}