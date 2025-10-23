package com.coffencode.onlinestoreapplication.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1️⃣ Extract token from the Authorization header
            String token = extractToken(request);

            if (token != null) {
                // 2️⃣ Extract username from the token
                String username = jwtUtil.extractUsername(token);

                // 3️⃣ Authenticate if context is clear
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    authenticateUser(token, username, request);
                }
            }
        } catch (Exception ex) {
            logger.error("JWT processing failed: {}", ex.getMessage());
        }

        // 4️⃣ Continue the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT from the Authorization header.
     */
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        logger.debug("Incoming request: {} | Auth header: {}", request.getRequestURI(), authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Validates the token and sets authentication in the context.
     */
    private void authenticateUser(String token, String username, HttpServletRequest request) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if (jwtUtil.isTokenValid(token, userDetails)) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("✅ Authenticated user: {}", username);
        } else {
            logger.warn("❌ Invalid JWT for user: {}", username);
        }
    }
}
