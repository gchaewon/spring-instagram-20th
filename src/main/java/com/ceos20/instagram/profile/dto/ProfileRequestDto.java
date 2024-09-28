package com.ceos20.instagram.profile.dto;

import com.ceos20.instagram.profile.domain.Gender;
import com.ceos20.instagram.profile.domain.Profile;
import com.ceos20.instagram.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileRequestDto {
    private String link;
    private String introduce;
    private Gender gender;
    private Boolean publicOption;
    private String profileImageUrl;

    @Builder
    public ProfileRequestDto(String link, String introduce, Gender gender,
                                   Boolean publicOption, String profileImageUrl) {
        this.link = link;
        this.introduce = introduce;
        this.gender = gender;
        this.publicOption = publicOption;
        this.profileImageUrl = profileImageUrl;
    }
    public Profile toEntity(User user) {
        return Profile.builder()
                .user(user) // User 객체를 매개변수로 받음
                .link(this.link)
                .introduce(this.introduce)
                .gender(this.gender)
                .publicOption(this.publicOption)
                .profileImageUrl(this.profileImageUrl)
                .build();
    }
}
