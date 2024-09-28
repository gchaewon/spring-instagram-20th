package com.ceos20.instagram.post.service;

import com.ceos20.instagram.image.repository.ImageRepository; // 추가
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.dto.PostRequestDto;
import com.ceos20.instagram.post.dto.PostResponseDto;
import com.ceos20.instagram.post.dto.PostUpdateRequestDto;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    // 포스트 생성 메서드
    @Transactional
    public PostResponseDto createPost(Long userId, PostRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다: " + userId));

        Post post = requestDto.toEntity(user);
        Post savedPost = postRepository.save(post);

        return PostResponseDto.from(savedPost, requestDto.getImageIdList()); // 수정
    }

    // 포스트 조회 메서드
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("포스트를 찾을 수 없습니다: " + postId));

        List<Long> imageIdList = imageRepository.findByPostId(postId); // Post에 포함된 이미지 ID 리스트를 가져오기
        return PostResponseDto.from(post, imageIdList); // 수정
    }

    // 포스트 수정 메서드
    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto updateDto) {
        // 포스트 아이디로 조회
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("포스트를 찾을 수 없습니다: " + postId));
        List<Long> imageIdList = imageRepository.findByPostId(postId);

        Post updatedPost = Post.builder()
                .id(existingPost.getId())
                .user(existingPost.getUser())
                .content(updateDto.getContent() != null ? updateDto.getContent() : existingPost.getContent()) // content 업데이트
                .commentOption(updateDto.getCommentOption() != null ? updateDto.getCommentOption() : existingPost.getCommentOption()) // 댓글 옵션 업데이트
                .build();

        // 포스트 저장
        Post savedPost = postRepository.save(updatedPost);
        return PostResponseDto.from(savedPost, imageIdList);
    }

    // 포스트 삭제 메서드
    @Transactional
    public void deletePost(Long postId) {
        // 포스트 아이디로 조회
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("포스트를 찾을 수 없습니다: " + postId));

        // 포스트 삭제
        postRepository.delete(existingPost);
    }

}