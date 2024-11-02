package com.ceos20.instagram.domain.follow.service;

import com.ceos20.instagram.domain.follow.domain.Follow;
import com.ceos20.instagram.domain.follow.dto.FollowResponseDto;
import com.ceos20.instagram.domain.follow.dto.UserInfoDto;
import com.ceos20.instagram.domain.follow.repository.FollowRepository;
import com.ceos20.instagram.domain.profile.domain.Profile;
import com.ceos20.instagram.domain.profile.repository.ProfileRepository;
import com.ceos20.instagram.domain.user.domain.User;
import com.ceos20.instagram.domain.user.repository.UserRepository;
import com.ceos20.instagram.global.exception.CustomException;
import com.ceos20.instagram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    // 팔로잉 관계 여부 확인 메서드
    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.findByFollowerIdAndFollowingId(followerId, followingId).isPresent();
    }

    // 팔로우 생성 메서드
    @Transactional
    public FollowResponseDto createFollow(Long followerId, Long followingId) {
        // 팔로우 요청한 유저 조회
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "팔로우 요청을 한 유저를 찾을 수 없습니다.", followerId));

        // 팔로우 요청 받을 유저 조회
        User following = userRepository.findById(followingId)
                .orElseThrow(() ->new CustomException(ErrorCode.NOT_FOUND, "팔로우 요청을 받을 유저를 찾을 수 없습니다..", followerId));

        // 이미 존재하는 팔로우 관계인지 확인
        if (isFollowing(followerId, followingId)) {
            throw new CustomException(ErrorCode.CONFLICT, "이미 팔로우 관계가 존재합니다.", followingId);
        }

        // 팔로우 생성 및 저장
        Follow follow = Follow.builder()
                .user(follower)
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);

        return FollowResponseDto.from(follow);
    }

    // 팔로잉 삭제 메서드 (내가 팔로우하는 유저 중 삭제)
    @Transactional
    public void deleteFollowing(Long userId, Long followingId) {
        // 삭제 요청한 유저 조회
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저의 요청입니다.", userId));

        // 팔로잉(내가 팔로우하는 유저) 아이디로 팔로우 관계 조회
        Follow follow = followRepository.findByFollowerIdAndFollowingId(userId, followingId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "팔로우 관계를 찾을 수 없습니다.", followingId));

        // 팔로우 관계 삭제
        followRepository.delete(follow);
    }

    // 팔로워 삭제 메서드 (나를 팔로우하는 유저 중 삭제)
    @Transactional
    public void deleteFollower(Long userId, Long followerId) {
        // 삭제 요청한 유저 조회
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저의 요청입니다.", userId));

        // 팔로워(나를 팔로우하는 유저) 아이디로 팔로우 관계 조회
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "팔로우 관계를 찾을 수 없습니다.", followerId));

        // 팔로우 관계 삭제
        followRepository.delete(follow);
    }

    // 특정 사용자를 팔로우하는 유저 (팔로워) 목록 조회
    public List<UserInfoDto> getFollowers(Long userId) {
        // 유저 조회
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "조회하려는 유저를 찾을 수 없습니다.", userId));

        // 프로필 조회 및 비공개 여부 확인
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "프로필을 찾을 수 없습니다.", userId));

        if (!profile.getPublicOption()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "해당 계정은 비공개 계정으로 조회가 불가합니다.", userId);
        }

        // 팔로워 조회
        List<Follow> followers = followRepository.findAllFollwerByUserId(userId);

        // DTO로 변환
        List<UserInfoDto> userInfoDtos = followers.stream()
                .map(follow -> {
                    User follower = follow.getFollower();
                    return UserInfoDto.builder()
                            .id(follower.getId())
                            .username(follower.getUsername())
                            .nickname(follower.getNickname())
                            .build();
                })
                .collect(Collectors.toList());

        return userInfoDtos;
    }

    // 특정 사용자가 팔로우한 유저 (팔로잉) 목록 조회
    public List<UserInfoDto>  getFollowings(Long userId) {
        // 유저 조회
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "조회하려는 유저를 찾을 수 없습니다.", userId));

        // 프로필 조회 및 비공개 여부 확인
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "프로필을 찾을 수 없습니다.", userId));

        if (!profile.getPublicOption()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "해당 계정은 비공개 계정으로 조회가 불가합니다.", userId);
        }

        // 팔로잉 조회
        List<Follow> followings = followRepository.findAllFollowingByUserId(userId);

        // DTO로 변환
        List<UserInfoDto> userInfoDtos = followings.stream()
                .map(follow -> {
                    User following = follow.getFollowing();
                    return UserInfoDto.builder()
                            .id(following.getId())
                            .username(following.getUsername())
                            .nickname(following.getNickname())
                            .build();
                })
                .collect(Collectors.toList());

        return userInfoDtos;
    }
}
