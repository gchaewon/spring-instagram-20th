package com.ceos20.instagram.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@AllArgsConstructor
public class ResponseTemplate {
    public int status;
    public String message;
    public static ResponseEntity<ResponseTemplate> toEntity(HttpStatus status, String message){
        return ResponseEntity
                .status(status)
                .body(ResponseTemplate.builder()
                    .message(message)
                    .status(status.value())
                .build());
    }
}
