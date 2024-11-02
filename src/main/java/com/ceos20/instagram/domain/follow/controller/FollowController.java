package com.ceos20.instagram.domain.follow.controller;

import com.ceos20.instagram.domain.follow.dto.FollowResponseDto;
import com.ceos20.instagram.domain.follow.dto.UserInfoDto;
import com.ceos20.instagram.domain.follow.service.FollowService;
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
@RequestMapping("/follows")
@Tag(name = "Follow Controller", description = "팔로우 컨트롤러 \n 생성, 삭제, 조회 로직을 포함합니다.")
public class FollowController {
    private final FollowService followService;

    // 팔로우 생성 (특정 유저 팔로우)
    @Operation(summary = "팔로우 생성")
    @PostMapping("/{followingId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로우 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public ResponseEntity<FollowResponseDto> createFollow(@PathVariable Long followingId){
        // 현재 로그인한 유저의 id 값을 가져오는 코드로 수정 예정
        Long followerId = 180L;
        FollowResponseDto responseDto = followService.createFollow(followerId, followingId);
        return ResponseEntity.ok(responseDto);
    }

    // 팔로잉 삭제 (내가 팔로우하는 유저 중 삭제)
    @Operation(summary = "팔로잉 삭제")
    @DeleteMapping("/{followingId}/followings")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "팔로잉 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 팔로잉입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public void deleteFollowing(@PathVariable Long followingId){
        // 현재 로그인한 유저의 id 값을 가져오는 코드로 수정 예정
        Long userId = 180L;
        followService.deleteFollowing(userId, followingId);
    }

    // 팔로워 삭제 (나를 팔로우하는 유저 중 삭제)
    @Operation(summary = "팔로워 삭제")
    @DeleteMapping("/{followerId}/followers")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "팔로워 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 팔로워입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public void deleteFollower(@PathVariable Long followerId){
        // 현재 로그인한 유저의 id 값을 가져오는 코드로 수정 예정
        Long userId = 180L;
        followService.deleteFollower(userId, followerId);
    }

    // 특정 사용자의 팔로잉 목록 조회
    @Operation(summary = "팔로잉 조회")
    @GetMapping("/{userId}/followings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로잉 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public ResponseEntity<List<UserInfoDto>> getFollowings(@PathVariable Long userId){
        List<UserInfoDto> followings = followService.getFollowings((userId));
        return ResponseEntity.ok(followings);
    }

    // 특정 사용자의 팔로워 목록 조회
    @Operation(summary = "팔로워 조회")
    @GetMapping("/{userId}/followers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팔로잉 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public ResponseEntity<List<UserInfoDto>> getFollowers(@PathVariable Long userId){
        List<UserInfoDto> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }
}

