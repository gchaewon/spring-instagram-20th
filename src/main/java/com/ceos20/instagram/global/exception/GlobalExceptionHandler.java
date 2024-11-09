package com.ceos20.instagram.global.exception;

import com.ceos20.instagram.global.ResponseTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseTemplate<?>> handleCustomException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        String message = exception.getMessage();
        Map<String, String> fieldErrors = exception.getFieldErrors();

        // 필드 오류가 있을 경우 필드 오류를 data로 전달
        ResponseTemplate<?> response;
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            response = ResponseTemplate.builder()
                    .status(errorCode.getHttpStatus().value())
                    .message(message)
                    .success(false)
                    .data(fieldErrors)
                    .build();
        } else {
            response = ResponseTemplate.builder()
                    .status(errorCode.getHttpStatus().value())
                    .message(message)
                    .success(false)
                    .build();
        }

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }
}
