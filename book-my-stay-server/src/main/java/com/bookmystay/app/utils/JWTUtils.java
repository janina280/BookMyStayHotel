package com.bookmystay.app.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class JWTUtils {

    private static final long EXPIRATION_MS = 1000L * 60 * 60 * 24 * 7; // 7 zile
    private SecretKey key;

    @Value("${jwt.secret}")
    private String secretString;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("iat", now.getEpochSecond())
                .claim("exp", exp.getEpochSecond())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Instant now = Instant.now();
        Instant expiration = Instant.ofEpochSecond(extractClaims(token).get("exp", Long.class));
        return now.isAfter(expiration);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
