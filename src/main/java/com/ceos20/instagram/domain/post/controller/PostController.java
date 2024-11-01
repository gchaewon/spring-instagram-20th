package com.ceos20.instagram.domain.post.controller;

import com.ceos20.instagram.domain.post.dto.PostRequestDto;
import com.ceos20.instagram.domain.post.dto.PostResponseDto;
import com.ceos20.instagram.domain.post.dto.PostUpdateRequestDto;
import com.ceos20.instagram.domain.post.service.PostService;
import com.ceos20.instagram.global.ResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 작성 성공",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = PostResponseDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 유저의 요청입니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ResponseTemplate.class))
        )
    })
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto){
        // 현재 유저의 id 값을 가져오는 코드로 수정 예정
        Long userId = 180L;
        PostResponseDto responseDto = postService.createPost(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 조회
    @Operation(summary = "게시글 조회")
    @GetMapping("/posts/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId){
        PostResponseDto responseDto = postService.getPost(postId);
        return ResponseEntity.ok(responseDto);
    }

    // 특정 유저의 게시글 목록 조회
    @Operation(summary = "특정 유저의 게시글 전체 조회")
    @GetMapping("/users/{userId}/posts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "특정 유저의 전체 게시글 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "작성한 글이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public ResponseEntity<List<PostResponseDto>> getPostsByUserId(@PathVariable Long userId){
        List<PostResponseDto> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    // 게시글 수정
    @Operation(summary = "게시글 수정")
    @PatchMapping("/posts/{postId}") // 필드 일부 수정 가능
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId,
                                                      @RequestBody PostUpdateRequestDto requestDto){
        PostResponseDto responseDto = postService.updatePost(postId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 게시글 삭제
    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/posts/{postId}")
    @ApiResponses({
            // 반환할 데이터 없음
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공")
    })
    public ResponseEntity<Void> deletePost(@PathVariable Long postId){
        postService.deletePost((postId));
        return ResponseEntity.noContent().build();
    }
}
