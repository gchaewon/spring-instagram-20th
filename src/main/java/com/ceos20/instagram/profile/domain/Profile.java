package com.ceos20.instagram.profile.domain;

import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
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

    private Boolean public_option;

    private String profile_image_url;
}

