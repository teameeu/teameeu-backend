package com.teameau.waymore.careernet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CareerNetReportRequest(
        @NotBlank(message = "심리검사번호는 필수입니다.")
        String qno,

        @NotBlank(message = "검사자 타입은 필수입니다.")
        String trgetSe,

        @NotBlank(message = "성별 코드는 필수입니다.")
        String gender,

        String school,

        @NotBlank(message = "학년은 필수입니다.")
        String grade,

        @NotNull(message = "검사 시작일시는 필수입니다.")
        Long startDtm,

        @NotBlank(message = "답변은 필수입니다.")
        String answers
) {
}
