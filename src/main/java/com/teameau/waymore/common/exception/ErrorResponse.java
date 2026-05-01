package com.teameau.waymore.common.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        List<String> details
) {
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getStatus().value(),
                errorCode.name(),
                errorCode.getMessage(),
                List.of()
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, List<String> details) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getStatus().value(),
                errorCode.name(),
                errorCode.getMessage(),
                details
        );
    }
}
