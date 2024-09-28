package com.ceos20.instagram.follow.dto;

import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowRequestDto {
    private Long followerId; // 팔로우하는 사용자 ID
    private Long followingId; // 팔로우되는 사용자 ID

    @Builder
    public FollowRequestDto(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    // Follow 엔티티로 변환하는 메서드
    public Follow toEntity(User follower, User following) {
        return Follow.builder()
                .follower(follower)
                .following(following)
                .build();
    }
}