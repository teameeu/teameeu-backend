package com.teameau.waymore.user.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 성적 입력 요청 dto
 */
public record GradeRequest(
        @NotBlank(message = "과목명은 필수입니다.")
        String subject,

        @NotBlank(message = "성적 입력은 필수입니다.")
        int score,

        @NotBlank(message = "등급 입력은 필수입니다.")
        String grade

        ) {

}
