package com.ceos20.instagram.domain.comment.dto;

import com.ceos20.instagram.domain.comment.domain.Comment;
import com.ceos20.instagram.domain.post.domain.Post;
import com.ceos20.instagram.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Builder
public class CommentRequestDto {
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
    private Long parentCommentId;

    // Comment 엔티티로 변환하는 메서드
    public static Comment toEntity(CommentRequestDto requestDto, Post post, User user, Comment parentComment) {
        return Comment.builder()
                .post(post)
                .user(user)
                .parentComment(parentComment)
                .content(requestDto.content)
                .build();
    }
}

