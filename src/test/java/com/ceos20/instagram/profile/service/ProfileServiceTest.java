package com.ceos20.instagram.profile.service;

import com.ceos20.instagram.profile.domain.Gender;
import com.ceos20.instagram.profile.domain.Profile;
import com.ceos20.instagram.profile.dto.ProfileRequestDto;
import com.ceos20.instagram.profile.dto.ProfileResponseDto;
import com.ceos20.instagram.profile.dto.ProfileUpdateRequestDto;
import com.ceos20.instagram.profile.repository.ProfileRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProfileServiceTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    private ProfileService profileService;
    private User user;
    private ProfileResponseDto createdProfileResponseDto;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService(profileRepository, userRepository);

        // 테스트용 사용자 생성 및 저장
        user = User.builder()
                .username("testUser")
                .nickname("nickname")
                .password("password")
                .email("test@gmail.com")
                .phone("010-1234-5678")
                .build();
        userRepository.saveAndFlush(user);

        // 프로필 생성
        ProfileRequestDto requestDto = ProfileRequestDto.builder()
                .link("http://example.com")
                .introduce("Bio about user")
                .gender(Gender.MALE)
                .publicOption(true)
                .profileImageUrl("http://example.com/image.jpg")
                .build();

        createdProfileResponseDto = profileService.createProfile(user.getId(), requestDto);
    }

    @Test
    @DisplayName("프로필 조회 테스트")
    void getProfileTest() {
        // when
        ProfileResponseDto responseDto = profileService.getProfile(user.getId());

        // then
        assertThat(responseDto.getLink()).isEqualTo("http://example.com");
        assertThat(responseDto.getIntroduce()).isEqualTo("Bio about user");
        assertThat(responseDto.getGender()).isEqualTo(Gender.MALE);
        assertThat(responseDto.getPublicOption()).isTrue();
        assertThat(responseDto.getProfileImageUrl()).isEqualTo("http://example.com/image.jpg");
    }

}
