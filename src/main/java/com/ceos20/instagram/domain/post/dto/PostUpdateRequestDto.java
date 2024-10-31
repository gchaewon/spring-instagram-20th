package com.ceos20.instagram.domain.post.dto;

import com.ceos20.instagram.domain.post.domain.CommentOption;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostUpdateRequestDto {
    private String content; // 포스트 내용
    private List<Long> imageIdList; // 업데이트할 이미지 id 리스트
    private CommentOption commentOption = CommentOption.ENABLED; // 댓글 허용으로 기본값 설정
}



