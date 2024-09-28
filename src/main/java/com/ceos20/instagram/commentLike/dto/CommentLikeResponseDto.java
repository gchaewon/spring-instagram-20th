package com.ceos20.instagram.commentLike.dto;

import com.ceos20.instagram.commentLike.domain.CommentLike;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentLikeResponseDto {
    private Long id;
    private Long commentId;
    private Long userId;
    private LocalDateTime createdAt;

    @Builder
    public CommentLikeResponseDto(Long id, Long commentId, Long userId, LocalDateTime createdAt) {
        this.id = id;
        this.commentId = commentId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // CommentLike 엔티티에서 DTO로 변환하는 메서드
    public static CommentLikeResponseDto from(CommentLike commentLike) {
        return CommentLikeResponseDto.builder()
                .id(commentLike.getId())
                .commentId(commentLike.getComment().getId())
                .userId(commentLike.getUser().getId())
                .createdAt(commentLike.getCreatedAt())
                .build();
    }
}

