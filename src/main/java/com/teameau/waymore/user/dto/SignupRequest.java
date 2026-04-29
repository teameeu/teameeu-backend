package com.teameau.waymore.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record SignupRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Size(max = 255, message = "이메일은 50자를 넘을 수 없습니다.") // TODO: DB 컬럼 50자로 수정
        String email,

        @NotBlank(message = "비밀번호 입력은 필수입니다.")
        @Size(min = 8, max = 255, message = "비밀번호 입력은 영문 ∙ 숫자 포함 8자 이상 입력해야 합니다.")
        String password,

        @NotBlank(message = "동일한 비밀번호를 입력해주세요.")
        @Size(min = 8, max = 30)
        String passwordCheck,

        @NotBlank(message = "생년월일을 입력해주세요.")
        @Past(message = "생년월일은 가입한 날짜 이전이어야 합니다.")
        LocalDate birthday,

        @NotBlank(message = "학과/부서는 필수입니다.")
        @Size(max = 20)
        String department,

        @NotBlank(message = "희망 진로는 필수입니다.")
        @Size(max = 20)
        String career
) {
}


