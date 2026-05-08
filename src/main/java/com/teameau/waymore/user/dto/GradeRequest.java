package com.teameau.waymore.user.dto;

import jakarta.validation.constraints.*;

/**
 * 성적 입력 요청 dto
 */
public record GradeRequest(
        @NotBlank(message = "과목명은 필수입니다.")
        @Size(max = 10, message = "과목명은 10자를 넘을 수 없습니다.")
        String subject,

        @NotNull(message = "성적 입력은 필수입니다.")
        @Min(value = 0, message = "성적은 0 이상이어야 합니다.")
        @Max(value = 100, message = "성적은 100 이하여야 합니다")
        Integer score,

        @NotBlank(message = "등급 입력은 필수입니다.")
        @Size(max = 10, message = "등급은 10자를 넘을 수 없습니다.")
        String grade

        ) {

}
