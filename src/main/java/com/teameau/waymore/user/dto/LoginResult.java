package com.teameau.waymore.user.dto;

import java.time.Duration;

public record LoginResult(
        LoginResponse response,
        String refreshToken,
        Duration refreshTokenMaxAge
) {
}
