package com.ceos20.instagram.domain.post.dto;

import com.ceos20.instagram.domain.image.domain.Image;
import com.ceos20.instagram.domain.post.domain.Post;
import com.ceos20.instagram.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostResponseDto {
    private Long id; // post 고유 번호
    private Long userId; // 작성자 고유 번호
    private List<Image> images; // 이미지 리스트
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Post 엔티티 -> DTO로 변환하는 정적 팩토리 메서드
    // service 레이어에서 이미지 리스트를 전달 받음
    public static PostResponseDto from(Post post, List<Image> images) {
        User user = post.getUser();
        return PostResponseDto.builder()
                .id(post.getId())
                .userId(user.getId()) // 작성자 ID
                .images(images)
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }
}
