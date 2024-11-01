package com.ceos20.instagram.domain.profile.service;

import com.ceos20.instagram.domain.profile.domain.Profile;
import com.ceos20.instagram.domain.profile.dto.ProfileRequestDto;
import com.ceos20.instagram.domain.profile.dto.ProfileResponseDto;
import com.ceos20.instagram.domain.profile.dto.ProfileUpdateRequestDto;
import com.ceos20.instagram.domain.profile.repository.ProfileRepository;
import com.ceos20.instagram.domain.user.domain.User;
import com.ceos20.instagram.domain.user.repository.UserRepository;
import com.ceos20.instagram.global.exception.CustomException;
import com.ceos20.instagram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    // 프로필 생성 메서드
    @Transactional
    public ProfileResponseDto createProfile(Long userId, ProfileRequestDto requestDto) {
        // userId로 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저로부터 요청입니다.", userId));

        // 프로필 조회
        Optional<Profile> existingProfile = profileRepository.findByUserId(userId);

        // 이미 프로필이 존재하는지 확인
        if (existingProfile.isPresent()) {
            throw new CustomException(ErrorCode.CONFLICT, "이미 프로필이 존재하는 유저입니다.", userId);
        }

        // 프로필 저장
        Profile profile = profileRepository.save(requestDto.toEntity(requestDto, user));

        // 프로필 생성 성공
        return ProfileResponseDto.from(profile);
    }

    // 프로필 조회 메서드
    public ProfileResponseDto getProfile(Long userId){
        // userId로 프로필 조회
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND, "프로필을 찾을 수 없습니다.", userId));

        // 프로필 조회 성공
        return ProfileResponseDto.from(profile);
    }

    // 프로필 수정 메서드
    @Transactional
    public ProfileResponseDto updateProfile(Long userId, ProfileRequestDto updateDto){
        // userId로 프로필 조회
        Profile existingProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "프로필을 찾을 수 없습니다.", userId));

        // 요청한 유저 ID와 프로필의 유저 ID가 같은지 확인
        if (!existingProfile.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "프로필 수정 권한이 없습니다.", userId);
        }

        // 프로필 업데이트
        Profile updatedProfile = Profile.builder()
                .id(existingProfile.getId())
                .user(existingProfile.getUser())
                // 부분 업데이트
                .link(updateDto.getLink() != null ? updateDto.getLink() : existingProfile.getLink())
                .introduce(updateDto.getIntroduce() != null ? updateDto.getIntroduce() : existingProfile.getIntroduce())
                .gender(updateDto.getGender() != null ? updateDto.getGender() : existingProfile.getGender())
                .publicOption(updateDto.getPublicOption() != null ? updateDto.getPublicOption() : existingProfile.getPublicOption())
                .profileImageUrl(updateDto.getProfileImageUrl() != null ? updateDto.getProfileImageUrl() : existingProfile.getProfileImageUrl())
                .build();

        // 프로필 저장
        Profile savedProfile = profileRepository.save(updatedProfile);

        // 업데이트 성공
        return ProfileResponseDto.from(savedProfile);
    }
}
