package com.ceos20.instagram.profile.domain;

import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="profile_id")
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    private String link;

    private String introduce;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Boolean publicOption;

    private String profileImageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public Profile(Long id, User user, String link, String introduce, Gender gender,
                   Boolean publicOption, String profileImageUrl,
                   LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.user = user;
        this.link = link;
        this.introduce = introduce;
        this.gender = gender;
        this.publicOption = publicOption;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

}

