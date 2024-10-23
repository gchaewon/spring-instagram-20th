package com.ceos20.instagram.domain.post.dto;

import com.ceos20.instagram.domain.post.domain.CommentOption;
import com.ceos20.instagram.domain.post.domain.Post;
import com.ceos20.instagram.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostUpdateRequestDto {
    private String content; // 포스트 내용
    private CommentOption commentOption; // 댓글 옵션


    public PostUpdateRequestDto(String content, CommentOption commentOption) {
        this.content = content;
        this.commentOption = commentOption;
    }

    public Post toEntity(User user) {
        return Post.builder()
                .user(user) // 포스트를 작성한 사용자
                .content(this.content)
                .commentOption(this.commentOption) // 댓글 옵션 설정
                .build();
    }
}
