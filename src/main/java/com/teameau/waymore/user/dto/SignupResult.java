package com.teameau.waymore.user.dto;

import java.time.Duration;

public record SignupResult(
        SignupResponse response,
        String refreshToken,
        Duration refreshTokenMaxAge
) {
}
