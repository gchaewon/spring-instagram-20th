package com.ceos20.instagram.follow.service;

import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.follow.dto.FollowRequestDto;
import com.ceos20.instagram.follow.repository.FollowRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우 생성 메서드
    @Transactional
    public void createFollow(FollowRequestDto requestDto) {
        User follower = userRepository.findById(requestDto.getFollowerId())
                .orElseThrow(() -> new IllegalArgumentException("팔로워를 찾을 수 없습니다: " + requestDto.getFollowerId()));

        User following = userRepository.findById(requestDto.getFollowingId())
                .orElseThrow(() -> new IllegalArgumentException("팔로잉을 찾을 수 없습니다: " + requestDto.getFollowingId()));

        Follow follow = requestDto.toEntity(follower, following);
        followRepository.save(follow);
    }

    // 팔로우 삭제 메서드 (특정 사용자가 팔로우한 사람 중 삭제)
    @Transactional
    public void deleteFollow(Long followId) {
        Follow follow = followRepository.findById(followId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우를 찾을 수 없습니다: " + followId));

        followRepository.delete(follow);
    }

    // 팔로워 삭제 메서드 (특정 사용자를 팔로우하는 사람 중 삭제)
    @Transactional
    public void deleteFollower(Long followerId, Long followingId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 팔로우 관계를 찾을 수 없습니다: followerId=" + followerId + ", followingId=" + followingId));

        followRepository.delete(follow);
    }

    // 특정 사용자를 팔로우하는 팔로워 목록 조회
    @Transactional(readOnly = true)
    public List<Follow> getFollowers(Long userId) {
        return followRepository.findAllFollwerByUserId(userId);
    }

    // 특정 사용자가 팔로우한 사용자 목록 조회
    @Transactional(readOnly = true)
    public List<Follow> getFollowings(Long userId) {
        return followRepository.findAllFollowingByUserId(userId);
    }
}
