package com.ceos20.instagram.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonInclude(JsonInclude.Include.NON_NULL) // null 값을 가진 필드를 제외
@Builder
public class ResponseTemplate<T> {
    public int status;
    public boolean success;
    public String message;
    public T data;

    public static <T> ResponseEntity<ResponseTemplate<T>> createTemplate(HttpStatus status, String message, T data) {
        ResponseTemplate<T> responseTemplate = ResponseTemplate.<T>builder()
                .status(status.value())
                .success(true)
                .message(message)
                .data(data)
                .build();

        return ResponseEntity
                .status(status)
                .body(responseTemplate);
    }
}
