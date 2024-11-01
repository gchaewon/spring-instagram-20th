package com.ceos20.instagram.domain.commentLike.controller;

import com.ceos20.instagram.domain.commentLike.dto.CommentLikeResponseDto;
import com.ceos20.instagram.domain.commentLike.service.CommentLikeService;
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
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name="CommentLike Controller", description =  "댓글 좋아요 컨트롤러 \n 생성, 삭제 조회 로직을 포함합니다.")
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    // 댓글 좋아요 생성
    @Operation(summary = "댓글 좋아요 생성")
    @PostMapping("/{commentId}/likes")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "댓글 좋아요 성공",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ResponseTemplate.class))
        ),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ResponseTemplate.class))
        )
    })

    public ResponseEntity<CommentLikeResponseDto> createCommentLike(@PathVariable Long commentId){
        // 추후 현재 로그인한 유저의 Id 가져오는 코드로 수정 예정
        Long userId = 180L;

        CommentLikeResponseDto responseDto = commentLikeService.createCommentLike(commentId, userId);
        return ResponseEntity.ok(responseDto);
    }

    // 댓글 좋아요 삭제
    @Operation(summary = "댓글 좋아요 삭제")
    @DeleteMapping("/{commentId}/likes")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "댓글 좋아요 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ResponseTemplate.class))
        )
    })

    public ResponseEntity<Void> deleteCommentLike(@PathVariable Long commentId){
        // 추후 현재 로그인한 유저의 Id 가져오는 코드로 수정 예정
        Long userId = 180L;

        commentLikeService.deleteCommentLike(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}

