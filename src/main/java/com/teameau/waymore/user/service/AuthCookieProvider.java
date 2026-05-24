package com.teameau.waymore.user.service;

import org.springframework.http.ResponseCookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

// MARK: - 쿠키 생성 클래스
@Component
public class AuthCookieProvider {
    private final String refreshTokenCookieName;
    private final String refreshTokenCookiePath;
    private final boolean refreshTokenCookieSecure;
    private final String refreshTokenCookieSameSite;

    public AuthCookieProvider(
            @Value("${auth.cookie.refresh-token.name}") String refreshTokenCookieName,
            @Value("${auth.cookie.refresh-token.path}") String refreshTokenCookiePath,
            @Value("${auth.cookie.refresh-token.secure}") boolean refreshTokenCookieSecure,
            @Value("${auth.cookie.refresh-token.same-site}") String refreshTokenCookieSameSite
    ) {
        this.refreshTokenCookieName = refreshTokenCookieName;
        this.refreshTokenCookiePath = refreshTokenCookiePath;
        this.refreshTokenCookieSecure = refreshTokenCookieSecure;
        this.refreshTokenCookieSameSite = refreshTokenCookieSameSite;
    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken, Duration maxAge) {
        return ResponseCookie.from(refreshTokenCookieName, refreshToken)
                .httpOnly(true)
                .secure(refreshTokenCookieSecure)
                .sameSite(refreshTokenCookieSameSite)
                .path(refreshTokenCookiePath)
                .maxAge(maxAge)
                .build();
    }

    public String getRefreshTokenCookieName() {
        return refreshTokenCookieName;
    }

    // 로그아웃 시 쿠키 제거
    public ResponseCookie createEmptyRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenCookieName, "")
                .httpOnly(true)
                .secure(refreshTokenCookieSecure)
                .sameSite(refreshTokenCookieSameSite)
                .path(refreshTokenCookiePath)
                .maxAge(0)
                .build();
    }

}
