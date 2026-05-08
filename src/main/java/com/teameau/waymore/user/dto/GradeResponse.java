package com.teameau.waymore.user.dto;

public record GradeResponse(
        Long gradeId,
        String subject,
        Integer score,
        String grade
) {
}
