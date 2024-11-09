package com.ceos20.instagram.domain.user.dto;

import com.ceos20.instagram.domain.user.domain.User;
import com.ceos20.instagram.global.config.jwt.JwtToken;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginResponseDto {
    private Long userId; // 유저 고유 번호
    private String username; // @user1234 같은 유저 id
    private String accessToken;
    private String refreshToken;

    @Builder
    public UserLoginResponseDto(Long userId, String username, String accessToken, String refreshToken){
        this.userId = userId;
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static UserLoginResponseDto from(User user, JwtToken jwtToken){
        return UserLoginResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
