package com.ceos20.instagram.post.dto;

import com.ceos20.instagram.post.domain.CommentOption;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    private String content; // 포스트 내용
    private CommentOption commentOption; // 댓글 옵션

    @Builder
    public PostUpdateRequestDto(String content, CommentOption commentOption) {
        this.content = content;
        this.commentOption = commentOption;
    }
}
