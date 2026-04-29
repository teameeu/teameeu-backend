package com.teameau.waymore.user.dto;

import com.teameau.waymore.user.domain.User;

import java.time.LocalDate;

public record SignupResponse(
        Long userId,
        String email,
        LocalDate birthday,
        String department,
        String career,
        String accessToken

) {
    public static SignupResponse of(User user, String accessToken) {
        return new SignupResponse(
                user.getId(),
                user.getEmail(),
                user.getBirthday(),
                user.getDepartment(),
                user.getCareer(),
                accessToken
        );
    }
}
