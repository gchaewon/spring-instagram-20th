package com.ceos20.instagram.domain.profile.domain;

import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
public class Profile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="profile_id")
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(length = 200)
    private String link;

    @Column(length = 150)
    private String introduce;

    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.NON; // 기본값 설정

    private Boolean publicOption = true; // 기본값 설정

    private String profileImageUrl;

    @Builder
    public Profile(Long id, User user, String link, String introduce, Gender gender,
                   Boolean publicOption, String profileImageUrl) {
        this.id = id;
        this.user = user;
        this.link = link;
        this.introduce = introduce;
        this.gender = gender;
        this.publicOption = publicOption;
        this.profileImageUrl = profileImageUrl;
    }

}

