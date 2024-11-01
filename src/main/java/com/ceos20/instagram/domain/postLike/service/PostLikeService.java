package com.ceos20.instagram.domain.postLike.service;

import com.ceos20.instagram.domain.post.domain.Post;
import com.ceos20.instagram.domain.postLike.domain.PostLike;
import com.ceos20.instagram.domain.postLike.dto.PostLikeResponseDto;
import com.ceos20.instagram.domain.postLike.repository.PostLikeRepository;
import com.ceos20.instagram.domain.post.repository.PostRepository;
import com.ceos20.instagram.domain.user.domain.User;
import com.ceos20.instagram.domain.user.repository.UserRepository;
import com.ceos20.instagram.global.exception.CustomException;
import com.ceos20.instagram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    // 게시글 좋아요 여부 조회 메서드
    public boolean getPostLike(Long postId, Long userId) {
        return postLikeRepository.findByPostIdAndUserId(postId, userId).isPresent();
    }


    // 게시글 좋아요 생성 메서드
    @Transactional
    public PostLikeResponseDto createPostLike(Long postId, Long userId) {
        // 포스트 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->new CustomException(ErrorCode.NOT_FOUND, "포스트를 찾을 수 없습니다.", postId));

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저로부터 요청입니다.", userId));

        // 이미 좋아요가 존재하는지 확인
        if (getPostLike(postId, userId)) {
            throw new CustomException(ErrorCode.CONFLICT, "이미 좋아요가 존재합니다.", postId); // 중복 생성 예외
        }

        // 좋아요 만들기
        PostLike postLike = PostLike.builder()
                .user(user)
                .post(post)
                .build();

        // 좋아요 저장
        PostLike savedPostLike = postLikeRepository.save(postLike);
        return PostLikeResponseDto.from(savedPostLike);
    }

    // 게시글 좋아요 삭제 메서드
    @Transactional
    public void deletePostLike(Long postId, Long userId) {
        // 포스트 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->new CustomException(ErrorCode.NOT_FOUND, "포스트를 찾을 수 없습니다.", postId));

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저로부터 요청입니다.", userId));

        // 좋아요 조회
        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() ->new CustomException(ErrorCode.NOT_FOUND, "삭제하려는 좋아요를 찾을 수 없습니다.", postId));

        // 좋아요 삭제
        postLikeRepository.delete(postLike);
    }
}
