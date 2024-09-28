package com.ceos20.instagram.postLike.service;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.postLike.domain.PostLike;
import com.ceos20.instagram.postLike.dto.PostLikeRequestDto;
import com.ceos20.instagram.postLike.dto.PostLikeResponseDto;
import com.ceos20.instagram.postLike.repository.PostLikeRepository;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 좋아요 생성 메서드
    @Transactional
    public PostLikeResponseDto createPostLike(Long userId, PostLikeRequestDto requestDto) {
        // 포스트 조회
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("포스트를 찾을 수 없습니다: " + requestDto.getPostId()));

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다: " + userId));

        // 좋아요 만들기
        PostLike postLike = PostLike.builder()
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        // 좋아요 저장
        PostLike savedPostLike = postLikeRepository.save(postLike);

        return PostLikeResponseDto.from(savedPostLike);
    }

    // 게시글 좋아요 삭제 메서드
    @Transactional
    public void deletePostLike(Long postLikeId) {
        // 좋아요 조회
        PostLike postLike = postLikeRepository.findById(postLikeId)
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 찾을 수 없습니다: " + postLikeId));

        // 좋아요 삭제
        postLikeRepository.delete(postLike);
    }
}
