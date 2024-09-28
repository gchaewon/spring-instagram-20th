package com.ceos20.instagram.user.service;

import com.ceos20.instagram.config.EncoderConfig;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.dto.UserRegisterRequestDto;
import com.ceos20.instagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(EncoderConfig.class)
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 비밀번호 암호화용 encoder

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder); // PasswordEncoder 주입
    }

    @Test
    @DisplayName("이미 사용 중인 이메일로 가입 요청 시 예외처리")
    void emailExceptionTest() {
        // given
        // 이미 등록된 이메일이 있는 상황 설정
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto("testUser", "Test Nickname", "password", "test@gmail.com", "010-1234-5678");
        userRepository.save(User.builder()
                .email("test@gmail.com")
                .username("anotherUser")
                .password(passwordEncoder.encode("password")) // 암호화된 비밀번호 저장
                .build());

        // when & then
        // 이메일 중복 체크 시 예외 발생
        assertThatThrownBy(() -> userService.register(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 사용 중인 이메일입니다");
    }

    @Test
    @DisplayName("회원가입 테스트")
    void registerTest() {
        // given
        // 회원 가입 요청 데이터 설정
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto("newUser", "New Nickname", "password", "newuser@gmail.com", "010-5678-1234");

        // when
        // 회원 가입 요청 처리
        userService.register(requestDto);

        // then
        // 이메일이 데이터베이스에 저장되었는지 검증
        assertTrue(userRepository.existsByEmail("newuser@gmail.com"));
    }
}
