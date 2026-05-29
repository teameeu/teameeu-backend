package com.teameau.waymore.careernet.exception;

import com.teameau.waymore.common.exception.ErrorCode;
import com.teameau.waymore.common.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CareerNetExceptionHandler {
    @ExceptionHandler(CareerNetApiException.class)
    public ResponseEntity<ErrorResponse> handleCareerNetApiException(CareerNetApiException exception) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST, List.of(exception.getMessage())));
    }
}
