package com.lab.springecommerce.config;

/*
    @project   spring-ecommerce
    @class     JwtAuthenticationFilter
    @version   1.0.0
    @since     17.11.2025
*/

import com.lab.springecommerce.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Перевіряємо чи є токен в заголовку
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                // Токен недійсний
                logger.debug("Invalid JWT token: " + e.getMessage());
            }
        }

        // Якщо токен валідний і користувач не авторизований
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            if (jwtUtil.validateToken(token, username)) {
                // Створюємо UserDetails (простий варіант)
                UserDetails userDetails = User.builder()
                        .username(username)
                        .password("") // Пароль не потрібен для JWT
                        .authorities(new ArrayList<>()) // Можна додати ролі пізніше
                        .build();

                // Створюємо Authentication
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Встановлюємо в Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
