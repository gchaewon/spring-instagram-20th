package com.ceos20.instagram.domain.postLike.controller;

import com.ceos20.instagram.domain.postLike.dto.PostLikeResponseDto;
import com.ceos20.instagram.domain.postLike.service.PostLikeService;
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

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "PostLike Controller", description = "게시글 좋아요 컨트롤러 \n 생성, 삭제 조회 로직을 포함합니다.")
public class PostLikeController{
    private final PostLikeService postLikeService;

    // 게시글 좋아요 생성
    @Operation(summary = "게시글 좋아요 추가")
    @PostMapping("/{postId}/likes")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "게시글 좋아요 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ResponseTemplate.class))
        )
        }
    )
    public ResponseEntity<PostLikeResponseDto> createLike(@PathVariable Long postId){
       PostLikeResponseDto responseDto = postLikeService.createPostLike(postId);
       return ResponseEntity.ok(responseDto);
    }

    // 게시글 좋아요 삭제
    @Operation(summary = "게시글 좋아요 삭제")
    @DeleteMapping("/{postId}/likes")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 좋아요 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    }
    )
    public ResponseEntity<Void> deleteLike(@PathVariable Long postId){
        postLikeService.deletePostLike(postId);
        return ResponseEntity.noContent().build();
    }
}
