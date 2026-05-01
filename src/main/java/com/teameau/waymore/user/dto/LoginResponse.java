package com.teameau.waymore.user.dto;

import com.teameau.waymore.user.domain.User;

// MARK: - 로그인 응답 dto
public record LoginResponse(
        Long userId,
        String email,
        String accessToken
) {
    public static LoginResponse of(User user, String accessToken) {
        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                accessToken
        );
    }
}
