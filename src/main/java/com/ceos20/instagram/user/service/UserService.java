package com.ceos20.instagram.user.service;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.dto.UserLoginRequestDto;
import com.ceos20.instagram.user.dto.UserLoginResponseDto;
import com.ceos20.instagram.user.dto.UserRegisterRequestDto;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입 메서드
    @Transactional
    public void register(UserRegisterRequestDto requestDto){
        // 이메일 중복 체크
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + requestDto.getEmail());
        }
        // username 중복 체크
        if(userRepository.existsByUsername(requestDto.getUsername())){
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다: " + requestDto.getUsername());
        }
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 유저 저장
        userRepository.save(requestDto.toEntity(encodedPassword));
    }

    // 로그인 메서드
    @Transactional(readOnly = true)
    public UserLoginResponseDto login(UserLoginRequestDto requestDto){
        // username으로 사용자 조회
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다: " + requestDto.getUsername()));


        // 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }

        // 로그인 성공
        return UserLoginResponseDto.from(user);
    }
}
