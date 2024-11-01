package com.ceos20.instagram.domain.profile.dto;

import com.ceos20.instagram.domain.profile.domain.Gender;
import com.ceos20.instagram.domain.profile.domain.Profile;
import com.ceos20.instagram.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileRequestDto {
    private String link; // 링크
    private String introduce; // 소개글
    private Gender gender; // 성별
    private Boolean publicOption; // 프로필 공개 여부
    private String profileImageUrl; // 프로필 이미지

    public static Profile toEntity(ProfileRequestDto dto, User user) {
        return Profile.builder()
                .user(user) // 프로필을 만든 사용자
                .link(dto.getLink())
                .introduce(dto.getIntroduce())
                .gender(dto.getGender())
                .publicOption(dto.getPublicOption())
                .profileImageUrl(dto.getProfileImageUrl())
                .build();
    }
}
