package com.ceos20.instagram.comment.dto;

import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateRequestDto {
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content; // 수정할 댓글 내용

    @Builder
    public CommentUpdateRequestDto(String content) {
        this.content = content;
    }
}

