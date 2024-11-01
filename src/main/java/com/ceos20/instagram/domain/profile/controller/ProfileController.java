package com.ceos20.instagram.domain.profile.controller;

import com.ceos20.instagram.domain.post.dto.PostUpdateRequestDto;
import com.ceos20.instagram.domain.profile.dto.ProfileRequestDto;
import com.ceos20.instagram.domain.profile.dto.ProfileResponseDto;
import com.ceos20.instagram.domain.profile.service.ProfileService;
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
@RequiredArgsConstructor
@RequestMapping("/profiles")
@Tag(name="Profile Controller", description =  "프로필 컨트롤러 \n 생성, 조회, 수정 로직을 포함합니다.")

public class ProfileController {
    private final ProfileService profileService;

    // 프로필 생성
    @Operation(summary = "프로필 생성")
    @PostMapping("")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "프로필 생성 성공",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ResponseTemplate.class))
        ),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ResponseTemplate.class))
        )
    })
    public ResponseEntity<ProfileResponseDto> createProfile(@RequestBody ProfileRequestDto requestDto){
        // 추후 현재 로그인한 유저의 Id 가져오는 코드로 수정 예정
        Long userId = 180L;
        ProfileResponseDto responseDto = profileService.createProfile(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 특정 유저의 프로필 조회
    @Operation(summary = "프로필 조회")
    @GetMapping("/{userId}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "프로필 생성 성공",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ResponseTemplate.class))
        ),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ResponseTemplate.class))
        )
    })
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long userId){
        ProfileResponseDto responseDto = profileService.getProfile(userId);
        return ResponseEntity.ok(responseDto);
    }

    // 프로필 수정
    @Operation(summary = "프로필 수정")
    @PatchMapping("/")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            ),
            @ApiResponse(responseCode = "401", description = "프로필 수정 권한이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
    })
    public ResponseEntity<ProfileResponseDto> updateProfile(@RequestBody ProfileRequestDto requestDto){
        // 현재 유저의 id 값을 가져오는 코드로 수정 예정
        Long userId = 181L;

        ProfileResponseDto responseDto = profileService.updateProfile(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
