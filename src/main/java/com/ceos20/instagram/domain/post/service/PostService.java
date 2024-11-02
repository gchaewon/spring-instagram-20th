package com.ceos20.instagram.domain.post.service;

import com.ceos20.instagram.domain.image.domain.Image;
import com.ceos20.instagram.domain.image.service.ImageService;
import com.ceos20.instagram.domain.post.domain.Post;
import com.ceos20.instagram.domain.post.dto.PostRequestDto;
import com.ceos20.instagram.domain.post.dto.PostUpdateRequestDto;
import com.ceos20.instagram.domain.user.repository.UserRepository;
import com.ceos20.instagram.domain.post.dto.PostResponseDto;
import com.ceos20.instagram.domain.post.repository.PostRepository;
import com.ceos20.instagram.domain.user.domain.User;
import com.ceos20.instagram.global.exception.CustomException;
import com.ceos20.instagram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    // 포스트 생성 메서드
    @Transactional
    public PostResponseDto createPost(Long userId, PostRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저로부터 요청입니다.", userId));

        // dto -> entity 변환
        Post post = requestDto.toEntity(requestDto, user);

        // 포스트 저장
        Post savedPost = postRepository.save(post);

        // 이미지 생성 및 저장
        List<Image> images = imageService.createImages(requestDto.getImageUrls(), savedPost);

        return PostResponseDto.from(savedPost, images); // response DTO 반환
    }

    // 포스트 조회 메서드
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "포스트를 찾을 수 없습니다.", postId));

        // 포스트 아이디와 맵핑되는 이미지 리스트
        List<Image> images = imageService.getImagesByPostId(postId);

        return PostResponseDto.from(post, images); // response DTO 반환
    }

    // 특정 유저의 포스트 전체 조회 메서드
    public List<PostResponseDto> getPostsByUserId(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저로부터 요청입니다.", userId));

        // 유저의 모든 포스트 조회
        List<Post> posts = postRepository.findByUserId(userId);

        // 조회한 포스트 ID 리스트
        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .collect(Collectors.toList());

        // 조회한 포스트의 이미지 전체 리스트
        List<List<Image>> images = imageService.getImagesByPostIds(
                posts.stream().map(Post::getId).collect(Collectors.toList()));

        // 조회한 포스트와 이미지로 response DTO 생성하기
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for(int i=0; i<posts.size(); i++){
            postResponseDtos.add(PostResponseDto.from(posts.get(i), images.get(i)));
        }
        return postResponseDtos;
    }

    // 포스트 수정 메서드
    @Transactional
    public PostResponseDto updatePost(Long postId, Long userId, PostUpdateRequestDto requestDto) {
        // 포스트 아이디로 조회
        Post targetPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "포스트를 찾을 수 없습니다.", postId));

        // 수정 요청 유저와 포스트 작성 유저가 같은지 확인
        Long writerId = targetPost.getUser().getId();
        if (!writerId.equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "포스트를 수정할 권한이 없습니다.", userId);
        }

        // 업데이트 요청 반영한 포스트 객체 생성
        Post updatedPost = Post.builder()
                .id(targetPost.getId())
                .user(targetPost.getUser())
                .content(requestDto.getContent() != null ? requestDto.getContent() : targetPost.getContent()) // content 업데이트
                .commentOption(requestDto.getCommentOption() != null ? requestDto.getCommentOption() : targetPost.getCommentOption()) // 댓글 옵션 업데이트
                .build();

        // 포스트 저장
        Post savedPost = postRepository.save(updatedPost);

        // 이미지 서비스 호출하여 이미지 삭제
        if (requestDto.getImageIdList() != null) { // 삭제할 이미지가 있을 때만 수행
            imageService.deleteImagesByIds(requestDto.getImageIdList());
        }
        // 이미지 객체 리스트 생성
        List<Image> images = imageService.getImagesByPostId(postId);

        return PostResponseDto.from(savedPost, images);
    }

    // 포스트 삭제 메서드
    @Transactional
    public void deletePost(Long postId, Long userId) {
        // 포스트 아이디로 조회
        Post targetPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "포스트를 찾을 수 없습니다.", postId));

        // 삭제 요청 유저와 포스트 작성 유저가 같은지 확인
        Long writerId = targetPost.getUser().getId();
        if (!writerId.equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "포스트를 삭제할 권한이 없습니다.", userId);
        }

        // 관련 이미지 삭제
        imageService.deleteImagesByPostId(postId);

        // 포스트 삭제
        postRepository.delete(targetPost);
    }
}