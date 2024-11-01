package com.ceos20.instagram.domain.profile.dto;

import com.ceos20.instagram.domain.profile.domain.Gender;
import com.ceos20.instagram.domain.profile.domain.Profile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponseDto {
    private Long id; // 프로필 고유 번호
    private String link;
    private String introduce;
    private Gender gender;
    private Boolean publicOption;
    private String profileImageUrl;

    public static ProfileResponseDto from(Profile profile) {
        return ProfileResponseDto.builder()
                .id(profile.getId())
                .link(profile.getLink())
                .introduce(profile.getIntroduce())
                .gender(profile.getGender())
                .publicOption(profile.getPublicOption())
                .profileImageUrl(profile.getProfileImageUrl())
                .build();
    }
}
