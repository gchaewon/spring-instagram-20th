package com.ceos20.instagram.profile.domain;

import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class) // @CreatedDate를 위한 어노테이션
public class Profile {
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

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
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

