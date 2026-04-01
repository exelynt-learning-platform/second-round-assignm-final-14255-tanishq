package com.ecommerce.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ✅ Generate Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extract Username
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // ✅ Extract Claims
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Validate Token (VERY IMPORTANT)
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);

            return (username.equals(userDetails.getUsername()) &&
                    !isTokenExpired(token));

        } catch (Exception e) {
            return false;
        }
    }

    // ✅ Check Expiry
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}