package com.ceos20.instagram.postLike.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostLikeRequestDto {
    private Long postId; // 좋아요를 누른 포스트의 ID
    private Long userId; // 좋아요를 누른 사용자 ID

    @Builder
    public PostLikeRequestDto(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
