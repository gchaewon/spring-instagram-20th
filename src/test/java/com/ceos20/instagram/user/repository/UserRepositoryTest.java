package com.ceos20.instagram.user.repository;

import com.ceos20.instagram.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
// 테스트용 내장 DB로 변경하지 않기 위한 어노테이션
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    public void setUp() {
        // 테스트용 사용자 생성
        user = User.builder()
                .username("test")
                .nickname("testNickname")
                .password("testPassword")
                .email("test@test.com")
                .phone("010-1234-5678")
                .build();
    }


    @Test
    @Transactional
    @DisplayName("User 저장하는 테스트")
    public void saveUserTest() {
        // given
        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    @Transactional
    @DisplayName("username으로 사용자 조회 테스트")
    public void findByUsernameTest() {
        // given
        userRepository.save(user);

        // when
        User foundUser = userRepository.findByUsername("test").orElse(null);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("test");
    }

    @Test
    @DisplayName("이메일 존재 여부 체크 테스트")
    public void existsByEmailTest() {
        // given
        userRepository.save(user);

        // when
        boolean exists = userRepository.existsByEmail("test@test.com");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("username 존재 여부 체크 테스트")
    public void existsByUsernameTest() {
        // given
        userRepository.save(user);

        // when
        boolean exists = userRepository.existsByUsername("test");

        // then
        assertThat(exists).isTrue();
    }

}