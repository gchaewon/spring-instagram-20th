package com.ceos20.instagram.commentLike.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentLikeRequestDto {
    private Long commentId;

    @Builder
    public CommentLikeRequestDto(Long commentId) {
        this.commentId = commentId;
    }
}
