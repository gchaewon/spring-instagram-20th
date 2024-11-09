package com.ceos20.instagram.domain.post.controller;

import com.ceos20.instagram.domain.post.dto.PostRequestDto;
import com.ceos20.instagram.domain.post.dto.PostResponseDto;
import com.ceos20.instagram.domain.post.dto.PostUpdateRequestDto;
import com.ceos20.instagram.domain.post.service.PostService;
import com.ceos20.instagram.global.AuthenticationUtils;
import com.ceos20.instagram.global.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "게시글 컨트롤러 \n 작성, 수정, 삭제, 조회 로직을 포함합니다.")
public class PostController {
    private final PostService postService;
    // 게시글 작성
    @Operation(summary = "게시글 작성")
    @PostMapping("/posts")
    public ResponseEntity<ResponseTemplate<PostResponseDto>> createPost(@RequestBody PostRequestDto requestDto){
        Long userId = AuthenticationUtils.getLoginUserId();
        PostResponseDto responseDto = postService.createPost(userId, requestDto);
        return ResponseTemplate.createTemplate(HttpStatus.CREATED, "게시글 작성 성공", responseDto);
    }

    // 게시글 조회
    @Operation(summary = "게시글 조회")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ResponseTemplate<PostResponseDto>> getPost(@PathVariable Long postId){
        PostResponseDto responseDto = postService.getPost(postId);
        return ResponseTemplate.createTemplate(HttpStatus.OK, "게시글 조회 성공", responseDto);
    }

    // 특정 유저의 게시글 목록 조회
    @Operation(summary = "특정 유저의 게시글 전체 조회")
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<ResponseTemplate<List<PostResponseDto>>> getPostsByUserId(@PathVariable Long userId){
        List<PostResponseDto> posts = postService.getPostsByUserId(userId);
        return ResponseTemplate.createTemplate(HttpStatus.OK, "게시글 전체 조회 성공", posts);
    }

    // 게시글 수정
    @Operation(summary = "게시글 수정")
    @PatchMapping("/posts/{postId}") // 필드 일부 수정 가능
    public  ResponseEntity<ResponseTemplate<PostResponseDto>> updatePost(@PathVariable Long postId,
                                                      @RequestBody PostUpdateRequestDto requestDto){
        Long userId = AuthenticationUtils.getLoginUserId();
        PostResponseDto responseDto = postService.updatePost(postId, userId, requestDto);
        return ResponseTemplate.createTemplate(HttpStatus.OK, "게시글 수정 성공", responseDto);
    }

    // 게시글 삭제
    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/posts/{postId}")
    public  ResponseEntity<ResponseTemplate<PostResponseDto>> deletePost(@PathVariable Long postId){
        Long userId = AuthenticationUtils.getLoginUserId();
        postService.deletePost(postId, userId);
        return ResponseTemplate.createTemplate(HttpStatus.OK, "게시글 삭제 성공", null);
    }
}
