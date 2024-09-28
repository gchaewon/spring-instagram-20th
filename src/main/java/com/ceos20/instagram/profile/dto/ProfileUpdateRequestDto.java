package com.ceos20.instagram.profile.dto;

import com.ceos20.instagram.profile.domain.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileUpdateRequestDto {
    private String link;
    private String introduce;
    private Gender gender;
    private Boolean publicOption;
    private String profileImageUrl;

    @Builder
    public ProfileUpdateRequestDto(String link, String introduce, Gender gender, Boolean publicOption, String profileImageUrl) {
        this.link = link;
        this.introduce = introduce;
        this.gender = gender;
        this.publicOption = publicOption;
        this.profileImageUrl = profileImageUrl;
    }
}
