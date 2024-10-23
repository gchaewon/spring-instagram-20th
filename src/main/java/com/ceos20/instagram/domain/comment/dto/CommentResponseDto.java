package com.ceos20.instagram.domain.comment.dto;

import com.ceos20.instagram.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id; // 댓글 고유 번호
    private Long postId; // 댓글이 달린 포스트 ID
    private Long userId; // 댓글 작성자 고유 번호
    private String content;

    @Builder
    public CommentResponseDto(Long id, Long postId, Long userId, String content) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }

    // Comment 엔티티로부터 DTO로 변환하는 메서드
    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .build();
    }
}

