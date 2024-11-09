package com.ceos20.instagram.domain.user.dto;

import com.ceos20.instagram.domain.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRegisterRequestDto {
    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(message = "아이디는 알파벳, 숫자, 특수 문자 '_, .'을 포함한 3~50자로 구성되어야합니다."
            , regexp = "^[a-z0-9_.]{3,50}$")

    private String username;

    @NotBlank(message = "이름은 필수입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(message = "비밀번호는 하나 이상의 알파벳, 숫자, 특수 문자를 포함한 8~15자로 구성되어야합니다.",
        regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}")
    private String password;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(message = "전화번호는 01*-****-**** 로 구성되어야합니다.",
        regexp = "^01[0-9]-[0-9]{3,4}-[0-9]{4}$")
    private String phone;

    public static User toEntity(UserRegisterRequestDto requestDto, String encodedPassword) {
        return User.builder()
                .username(requestDto.getUsername())
                .nickname(requestDto.getNickname())
                .password(encodedPassword) // 암호화된 비밀번호 저장
                .email(requestDto.getEmail())
                .phone(requestDto.getPhone())
                .build();
    }
}