package com.teameau.waymore.user.controller;

import com.teameau.waymore.user.dto.*;
import com.teameau.waymore.user.service.AuthCookieProvider;
import com.teameau.waymore.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import com.teameau.waymore.common.exception.BusinessException;
import com.teameau.waymore.common.exception.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "인증", description = "로그인/로그아웃 및 회원가입 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthCookieProvider authCookieProvider;

    @PostMapping("/join")
    @Operation(summary = "회원가입")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        SignupResult result = authService.signup(request);
        ResponseCookie refreshTokenCookie = authCookieProvider.createRefreshTokenCookie(
                result.refreshToken(),
                result.refreshTokenMaxAge()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(result.response());
    }


    @PostMapping("/login")
    @Operation(summary = "로그인" , description = "JWT 토큰 발급")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResult result = authService.login(request);
        ResponseCookie refreshTokenCookie = authCookieProvider.createRefreshTokenCookie(
                result.refreshToken(),
                result.refreshTokenMaxAge()
        );

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(result.response());
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급")
    public ResponseEntity<LoginResponse> refresh(HttpServletRequest request) {
        String refreshToken = extractRefreshToken(request);
        LoginResult result = authService.refresh(refreshToken);
        ResponseCookie refreshTokenCookie = authCookieProvider.createRefreshTokenCookie(
                result.refreshToken(),
                result.refreshTokenMaxAge()
        );

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(result.response());
    }

    // 로그아웃
    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃",
            description = "refresh 토큰 삭제됨",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Long userId) {
        authService.logout(userId);
        ResponseCookie emptyCookie = authCookieProvider.createEmptyRefreshTokenCookie();
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, emptyCookie.toString()).build();
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        String refreshTokenCookieName = authCookieProvider.getRefreshTokenCookieName();
        for (Cookie cookie : cookies) {
            if (refreshTokenCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new BusinessException(ErrorCode.INVALID_REQUEST);
    }

}
