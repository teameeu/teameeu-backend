package com.teameau.waymore.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponseDto<T> {
    private String status;
    private T data;
    private String message;

    public static <T> CommonResponseDto<T> success(T data) {
        return CommonResponseDto.<T>builder()
                .status("success")
                .data(data)
                .message("요청이 성공적으로 처리되었습니다.")
                .build();
    }

    public static <T> CommonResponseDto<T> success(T data, String message) {
        return CommonResponseDto.<T>builder()
                .status("success")
                .data(data)
                .message(message)
                .build();
    }

    public static CommonResponseDto<Void> success() {
        return success(null);
    }

    public static CommonResponseDto<Void> error(String message) {
        return CommonResponseDto.<Void>builder()
                .status("error")
                .data(null)
                .message(message)
                .build();
    }
}
