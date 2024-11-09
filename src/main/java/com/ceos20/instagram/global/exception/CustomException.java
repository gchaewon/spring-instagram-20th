package com.ceos20.instagram.global.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, String> fieldErrors;

    // 기본 커스텀 Exception
    public CustomException(ErrorCode errorCode, String message, Object target) {
        super(String.format("%s %s", message, target)); // 메시지에 타겟 정보를 포함
        this.errorCode = errorCode;
        this.fieldErrors = null;
    }
    // DTO 필드 형식 오류 커스텀 Exception
    public CustomException(ErrorCode errorCode, String message,  Map<String, String> fieldErrors) {
        super(message);
        this.errorCode = errorCode;
        this.fieldErrors = fieldErrors;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
