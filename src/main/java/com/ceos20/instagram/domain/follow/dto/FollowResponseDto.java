package com.ceos20.instagram.domain.follow.dto;

import com.ceos20.instagram.domain.follow.domain.Follow;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowResponseDto {
    private Long id; // 생성된 팔로우 ID
    private Long followerId; // 팔로워 ID (팔로우 요청자)
    private Long followingId; // 팔로잉 ID (팔로우 요청을 받은사람)

    @Builder
    public FollowResponseDto(Long id, Long followerId, Long followingId) {
        this.id = id;
        this.followerId = followerId;
        this.followingId = followingId;
    }

    // Follow 엔티티로부터 DTO로 변환하는 메서드
    public static FollowResponseDto from(Follow follow) {
        return FollowResponseDto.builder()
                .id(follow.getId())
                .followerId(follow.getFollower().getId())
                .followingId(follow.getFollowing().getId())
                .build();
    }
}