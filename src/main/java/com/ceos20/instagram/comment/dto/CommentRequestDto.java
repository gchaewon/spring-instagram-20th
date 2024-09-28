package com.ceos20.instagram.comment.dto;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    @Builder
    public CommentRequestDto(String content) {
        this.content = content;
    }

    // Comment 엔티티로 변환하는 메서드
    public Comment toEntity(Post post, User user) {
        return Comment.builder()
                .post(post) // 댓글이 달린 포스트
                .user(user) // 댓글 작성자
                .content(this.content)
                .build();
    }
}

