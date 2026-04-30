package com.teameau.waymore.user.controller;

import com.teameau.waymore.user.dto.SignupRequest;
import com.teameau.waymore.user.dto.SignupResponse;
import com.teameau.waymore.user.dto.SignupResult;
import com.teameau.waymore.user.service.AuthCookieProvider;
import com.teameau.waymore.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthCookieProvider authCookieProvider;

    // 회원가입
    @PostMapping("/join")
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
}
