package com.ceos20.instagram.profile.dto;

import com.ceos20.instagram.profile.domain.Gender;
import com.ceos20.instagram.profile.domain.Profile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponseDto {
    private Long id; // 프로필 고유 번호
    private String link;
    private String introduce;
    private Gender gender;
    private Boolean publicOption;
    private String profileImageUrl;

    @Builder
    public ProfileResponseDto(Long id, String link, String introduce, Gender gender, Boolean publicOption, String profileImageUrl) {
        this.id = id;
        this.link = link;
        this.introduce = introduce;
        this.gender = gender;
        this.publicOption = publicOption;
        this.profileImageUrl = profileImageUrl;
    }
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
