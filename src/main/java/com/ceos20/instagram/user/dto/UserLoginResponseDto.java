package com.ceos20.instagram.user.dto;

import com.ceos20.instagram.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginResponseDto {
    private Long userId; // 유저 고유 번호
    private String username; // @user1234 같은 유저 id

    @Builder
    public UserLoginResponseDto(Long userId, String username){
        this.userId = userId;
        this.username = username;
    }

    public static UserLoginResponseDto from(User user){
        return UserLoginResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .build();
    }
}
