package com.ceos20.instagram.domain.follow.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {
    private Long id; // 유저 고유 번호
    private String username; // 유저 아이디 (@abcd)
    private String nickname; // 유저 이름 (김철수)
}
