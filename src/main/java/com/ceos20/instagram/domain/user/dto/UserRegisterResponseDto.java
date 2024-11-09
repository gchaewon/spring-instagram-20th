package com.ceos20.instagram.domain.user.dto;

import com.ceos20.instagram.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRegisterResponseDto {
    private Long userId; // 유저 고유 번호
    private String username; // @user1234 같은 유저 아이디

    public static UserRegisterResponseDto from(User user){
        return UserRegisterResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .build();
    }
}
