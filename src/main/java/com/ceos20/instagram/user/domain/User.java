package com.ceos20.instagram.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter

public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(length = 50)
    private String username;

    @NotNull
    @Column(length = 50, unique = true)
    private String nickname;

    @NotNull
    private String password; // 암호화되어 저장할 수 있으므로 길이 지정하지 않음

    @NotNull
    @Column(length = 254, unique = true)
    private String email;

    @NotNull
    @Column(length = 20)
    private String phone;

    @Builder
    public User(Long id, String username, String nickname, String password, String email, String phone) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

}

