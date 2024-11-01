package com.ceos20.instagram.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    // ErrorCode 받아서 예외 메시지 저장
    public CustomException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    // 에러 추가 정보를 받아 저장
    public CustomException(ErrorCode errorCode, String message, Object target) {
        super(String.format("%s - %s", message, target)); // 메시지에 타겟 정보를 포함
        this.errorCode = errorCode;
    }
}
