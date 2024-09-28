package com.ceos20.instagram.postLike.dto;

import com.ceos20.instagram.postLike.domain.PostLike;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostLikeResponseDto {
    private Long id; // 좋아요 고유 ID
    private Long postId; // 좋아요가 눌린 포스트 ID
    private Long userId; // 좋아요를 누른 사용자 ID

    @Builder
    public PostLikeResponseDto(Long id, Long postId, Long userId) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
    }

    // PostLike 엔티티로부터 DTO로 변환하는 메서드
    public static PostLikeResponseDto from(PostLike postLike) {
        return PostLikeResponseDto.builder()
                .id(postLike.getId())
                .postId(postLike.getPost().getId()) // 포스트 ID
                .userId(postLike.getUser().getId()) // 사용자 ID
                .build();
    }
}
