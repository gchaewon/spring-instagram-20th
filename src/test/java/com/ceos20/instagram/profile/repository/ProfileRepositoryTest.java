package com.ceos20.instagram.profile.repository;

import com.ceos20.instagram.profile.domain.Gender;
import com.ceos20.instagram.profile.domain.Profile;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProfileRepositoryTest {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Profile profile;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성
        user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .password("password")
                .build();
        userRepository.save(user);

        // 테스트용 프로필 생성
        profile = Profile.builder()
                .user(user)
                .link("http://test.com")
                .introduce("testuser 입니다.")
                .gender(Gender.MALE)
                .publicOption(true)
                .profileImageUrl("http://testuser.com/image.jpg")
                .build();
        profileRepository.save(profile);
    }

    @Test
    @DisplayName("userId로 프로필 조회하기")
    void findByUserId() {
        // given
        Long userId = user.getId();

        // when
        // 프로필 조회
        Optional<Profile> foundProfile = profileRepository.findByUserId(userId);

        // then
        assertThat(foundProfile).isPresent(); // 프로필이 존재하는지 확인
        assertThat(foundProfile.get().getUser()).isEqualTo(user); // 프로필이 같은지 확인
    }

    @Test
    @DisplayName("프로필 업데이트하기")
    void updateProfile() {
        // given
        Long profileId = profile.getId();
        String updatedLink = "http://update.com";
        String updatedIntroduce = "업데이트된 사용자입니다.";

        // 프로필 업데이트 (링크, 사용자, 수정 시간)
        Profile updatedProfile = Profile.builder()
                .id(profileId)
                .user(user)
                .link(updatedLink)
                .introduce(updatedIntroduce)
                .gender(profile.getGender())
                .publicOption(profile.getPublicOption())
                .profileImageUrl(profile.getProfileImageUrl())
                .modifiedAt(LocalDateTime.now())
                .build();

        // 프로필 저장
        profileRepository.save(updatedProfile);

        // when
        // 업데이트된 프로필 조회
        Optional<Profile> retrievedProfile = profileRepository.findById(profileId);

        // then
        assertThat(retrievedProfile).isPresent(); // 업데이트된 프로필이 존재하는지 확인
        assertThat(retrievedProfile.get().getLink()).isEqualTo(updatedLink); // 링크 수정 확인
        assertThat(retrievedProfile.get().getIntroduce()).isEqualTo(updatedIntroduce); // 소개 수정 확인
    }
}
