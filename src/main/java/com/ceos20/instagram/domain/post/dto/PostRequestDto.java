package com.ceos20.instagram.domain.post.dto;

import com.ceos20.instagram.domain.post.domain.CommentOption;
import com.ceos20.instagram.domain.post.domain.Post;
import com.ceos20.instagram.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Builder
public class PostRequestDto {
    @NotBlank(message = "내용은 필수입니다.")
    private String content; // 포스트 내용

    private List<Long> imageIdList; // 연결된 이미지 ID 리스트
    private CommentOption commentOption = CommentOption.ENABLED; // 댓글 허용으로 기본값 설정

    // Post 엔티티로 변환하는 메서드
    public Post toEntity(User user) {
        return Post.builder()
                .user(user) // 포스트를 작성한 사용자
                .content(this.content)
                .commentOption(this.commentOption) // 댓글 옵션 설정
                .build();
    }
}
