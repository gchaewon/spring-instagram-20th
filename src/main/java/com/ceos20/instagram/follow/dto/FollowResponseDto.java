package com.ceos20.instagram.follow.dto;

import com.ceos20.instagram.follow.domain.Follow;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowResponseDto {
    private Long id; // 팔로우 ID
    private Long followerId; // 팔로워 ID
    private Long followingId; // 팔로잉 ID

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