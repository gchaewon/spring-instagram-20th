package com.ceos20.instagram.user.dto;

import com.ceos20.instagram.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterRequestDto {
    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(message = "잘못된 아이디 형식입니다."
            , regexp = "^[a-z0-9_.]{3,50}$") //  알파벳, 숫자,  특수 문자 (_, .)포함, 3~ 50자

    private String username;

    @NotBlank(message = "이름은 필수입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(message = "잘못된 비밀번호 형식입니다.",
        regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}") // 반드시 하나 이상 알파벳, 숫자, 특수 문자 포함 8~15자
    private String password;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(message = "잘못된 전화번호 형식입니다.",
        regexp = "^01[0-9]-[0-9]{3,4}-[0-9]{4}$")
    private String phone;

    @Builder
    public UserRegisterRequestDto(String username, String nickname, String password, String email, String phone) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public User toEntity(String encodedPassword) {
        return User.builder()
                .username(this.username)
                .nickname(this.nickname)
                .password(encodedPassword) // 암호화된 비밀번호 저장
                .email(this.email)
                .phone(this.phone)
                .build();
    }

}