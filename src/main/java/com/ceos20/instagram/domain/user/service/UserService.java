package com.ceos20.instagram.domain.user.service;

import com.ceos20.instagram.domain.user.dto.UserRegisterRequestDto;
import com.ceos20.instagram.domain.user.domain.User;
import com.ceos20.instagram.domain.user.dto.UserLoginRequestDto;
import com.ceos20.instagram.domain.user.dto.UserLoginResponseDto;
import com.ceos20.instagram.domain.user.dto.UserRegisterResponseDto;
import com.ceos20.instagram.domain.user.repository.UserRepository;
import com.ceos20.instagram.global.config.jwt.JwtToken;
import com.ceos20.instagram.global.config.jwt.JwtTokenProvider;
import com.ceos20.instagram.global.exception.CustomException;
import com.ceos20.instagram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    // 회원 가입 메서드
    @Transactional
    public UserRegisterResponseDto register(UserRegisterRequestDto requestDto){
        // 이메일 중복 체크
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.CONFLICT, "이미 사용 중인 이메일입니다.", requestDto.getEmail());
        }
        // username 중복 체크
        if(userRepository.existsByUsername(requestDto.getUsername())){
            throw new CustomException(ErrorCode.CONFLICT, "이미 사용 중인 아이디입니다.", requestDto.getUsername());
        }
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 유저 저장
        User user = requestDto.toEntity(requestDto, encodedPassword);
        userRepository.save(user);

        return UserRegisterResponseDto.from(user);
    }

    // 로그인 메서드
    @Transactional(readOnly = true)
    public UserLoginResponseDto login(UserLoginRequestDto requestDto){
        // username으로 사용자 조회
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "존재하지 않는 아이디입니다." ,requestDto.getUsername()));

        // 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.NOT_FOUND, "비밀번호가 맞지 않습니다." ,requestDto.getPassword());
        }

        // UsernamePasswordAuthenticationToken 생성하여 인증 매니저에 넘기기
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), requestDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 인증 정보로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        // 로그인 성공 응답
        return UserLoginResponseDto.from(user, jwtToken);
    }
    // 아이디 중복 확인
    public boolean checkUsername(String username){
        return !userRepository.existsByUsername(username);
    }

}
