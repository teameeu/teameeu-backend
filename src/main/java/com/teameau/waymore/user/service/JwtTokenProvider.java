package com.teameau.waymore.user.service;

import com.teameau.waymore.user.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

// MARK: - JWT access, refresh 생성용
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final Duration accessTokenExpiration;
    private final Duration refreshTokenExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
            @Value("${jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = Duration.ofMillis(accessTokenExpirationMs);
        this.refreshTokenExpiration = Duration.ofMillis(refreshTokenExpirationMs);
    }

    // access token 생성
    public String createAccessToken(User user) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + accessTokenExpiration.toMillis());

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("email",user.getEmail())
                .issuedAt(now)
                .expiration(expiredAt)
                .signWith(secretKey)
                .compact();
    }

    // refresh token 생성
    public String createRefreshToken(User user) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + refreshTokenExpiration.toMillis());

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(expiredAt)
                .signWith(secretKey)
                .compact();
    }

    public Duration getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}
