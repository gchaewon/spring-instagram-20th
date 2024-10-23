package com.ceos20.instagram.domain.post.dto;

import com.ceos20.instagram.domain.post.domain.CommentOption;
import com.ceos20.instagram.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostResponseDto {
    private Long id; // post 고유 번호
    private Long userId; // 작성자 고유 번호
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private CommentOption commentOption;
    private List<Long> imageIdList; // 연결된 이미지 ID 리스트

    // Post 엔티티로부터 DTO로 변환하는 메서드
    public static PostResponseDto from(Post post, List<Long> imageIdList) { // imageIdList를 인자로 받음
        return PostResponseDto.builder()
                .id(post.getId())
                .userId(post.getUser().getId()) // 작성자 ID
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .commentOption(post.getCommentOption())
                .imageIdList(imageIdList)
                .build();
    }
}
