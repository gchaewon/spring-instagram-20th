package com.ceos20.instagram.post.dto;

import com.ceos20.instagram.post.domain.CommentOption;
import com.ceos20.instagram.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long id; // post 고유 번호
    private Long userId; // 작성자 고유 번호
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private CommentOption commentOption;
    private List<Long> imageIdList; // 연결된 이미지 ID 리스트

    @Builder
    public PostResponseDto(Long id, Long userId, String content, LocalDateTime createdAt,
                           LocalDateTime modifiedAt, CommentOption commentOption, List<Long> imageIdList) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.commentOption = commentOption;
        this.imageIdList = imageIdList;
    }

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
