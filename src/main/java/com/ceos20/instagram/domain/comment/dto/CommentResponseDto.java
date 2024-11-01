package com.ceos20.instagram.domain.comment.dto;

import com.ceos20.instagram.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    private Long id; // 댓글 고유 번호
    private Long userId; // 댓글 작성자 고유 번호
    private Long postId; // 댓글이 달린 포스트 ID
    private Long parentId; // 부모 댓글 아이디

    private String content; // 댓글 내용
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime modifiedAt; // 수정 시간

    // Comment 엔티티로부터 DTO로 변환하는 메서드
    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .postId(comment.getPost().getId())
                // parentComment가 없으면 getId를 호출했을 때 null이 나오도록 설정
                .parentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}

