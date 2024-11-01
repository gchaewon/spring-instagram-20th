package com.ceos20.instagram.domain.comment.controller;


import com.ceos20.instagram.domain.comment.dto.CommentRequestDto;
import com.ceos20.instagram.domain.comment.dto.CommentResponseDto;
import com.ceos20.instagram.domain.comment.service.CommentService;
import com.ceos20.instagram.domain.post.dto.PostRequestDto;
import com.ceos20.instagram.domain.post.dto.PostResponseDto;
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
@Tag(name ="Comment Controller", description="댓글 컨트롤러 \n 작성, 수정, 삭제, 조회 로직을 포함합니다.")
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @Operation(summary = "댓글 작성")
    @PostMapping("/posts/{postId}/comments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 작성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto requestDto,
                                                            @PathVariable Long postId){
        // 현재 유저의 id 값을 가져오는 코드로 수정 예정
        Long userId = 180L;
        CommentResponseDto responseDto = commentService.createComment(requestDto, postId, userId);
        return ResponseEntity.ok(responseDto);
    }

    // 특정 글의 댓글 전체 조회
    @Operation(summary = "댓글 전체 조회")
    @GetMapping("/posts/{postId}/comments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 전체 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId){
        List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 대댓글 전체 조회
    @Operation(summary = "대댓글 전체 조회")
    @GetMapping("/comments/{commentId}/recomments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대댓글 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public ResponseEntity<List<CommentResponseDto>> getRecomments(@PathVariable Long commentId){
        List<CommentResponseDto> comments = commentService.getRecomments(commentId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @Operation(summary = "댓글 수정")
    @PutMapping("/comments/{commentId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            ),
            @ApiResponse(responseCode = "401", description = "댓글 수정 권한이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public ResponseEntity<CommentResponseDto> updateComment(@RequestBody CommentRequestDto requestDto,
                                                            @PathVariable Long commentId){
        Long userId = 180L; // 로그인한 유저 아이디 가져오는 코드로 변경 필요

        // 댓글 수정
        CommentResponseDto updatedComment = commentService.updateComment(requestDto, commentId, userId);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            ),
            @ApiResponse(responseCode = "401", description = "댓글 삭제 권한이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public ResponseEntity<CommentResponseDto> deleteComment(@PathVariable Long commentId){
        Long userId = 180L; // 로그인한 유저 아이디 가져오는 코드로 변경 필요

        // 댓글 삭제
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}
