package com.ceos20.instagram.profile.service;

import com.ceos20.instagram.profile.domain.Profile;
import com.ceos20.instagram.profile.dto.ProfileRequestDto;
import com.ceos20.instagram.profile.dto.ProfileResponseDto;
import com.ceos20.instagram.profile.dto.ProfileUpdateRequestDto;
import com.ceos20.instagram.profile.repository.ProfileRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    // 프로필 생성 메서드
    @Transactional
    public ProfileResponseDto createProfile(Long userId, ProfileRequestDto requestDto) {
        // userId로 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다: " + userId));

        // 프로필 저장
        Profile profile = profileRepository.save(requestDto.toEntity(user));

        // 프로필 생성 성공
        return ProfileResponseDto.from(profile);
    }

    // 프로필 조회 메서드
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long userId){
        // userId로 프로필 조회
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("프로필을 찾을 수 없습니다: "+ userId));

        // 프로필 조회 성공
        return ProfileResponseDto.from(profile);
    }

    // 프로필 수정 메서드
    @Transactional
    public ProfileResponseDto updateProfile(Long userId, ProfileUpdateRequestDto updateDto){
        // userId로 프로필 조회
        Profile existingProfile = profileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다: " + userId));

        // 프로필 업데이트
        Profile updatedProfile = Profile.builder()
                .id(existingProfile.getId())
                .user(existingProfile.getUser())
                // 부분 업데이트 가능
                .link(updateDto.getLink() != null ? updateDto.getLink() : existingProfile.getLink())
                .introduce(updateDto.getIntroduce() != null ? updateDto.getIntroduce() : existingProfile.getIntroduce())
                .gender(updateDto.getGender() != null ? updateDto.getGender() : existingProfile.getGender())
                .publicOption(updateDto.getPublicOption() != null ? updateDto.getPublicOption() : existingProfile.getPublicOption())
                .profileImageUrl(updateDto.getProfileImageUrl() != null ? updateDto.getProfileImageUrl() : existingProfile.getProfileImageUrl())
                .modifiedAt(LocalDateTime.now()) // 수정 시간 반영
                .build();

        // 프로필 저장
        Profile savedProfile = profileRepository.save(updatedProfile);

        // 업데이트 성공
        return ProfileResponseDto.from(savedProfile);
    }
}
