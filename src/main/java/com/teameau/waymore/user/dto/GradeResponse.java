package com.teameau.waymore.user.dto;

import com.teameau.waymore.user.domain.Grade;

public record GradeResponse(
        Long gradeId,
        String subject,
        Integer score,
        String grade
) {

    public static GradeResponse from(Grade grade) {
        return new GradeResponse(
                grade.getGradeId(),
                grade.getSubject(),
                grade.getScore(),
                grade.getGrade()
        );
    }
}
