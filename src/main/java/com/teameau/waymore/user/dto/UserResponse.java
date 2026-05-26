package com.teameau.waymore.user.dto;

import com.teameau.waymore.user.domain.User;

public record UserResponse(
        Long userId,
        String userName,
        String department,
        String career

) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getDepartment(),
                user.getCareer()
        );
    }
}
