package com.ceos20.instagram.image.repository;

import com.ceos20.instagram.image.domain.Image;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private Post post;
    private User user;

    @BeforeEach
    void setUp() {
        // 테스트를 위한 유저 생성 및 저장
        user = User.builder().email("test@example.com").username("testuser").password("password").build();
        userRepository.save(user);

        // 테스트를 위한 게시글 생성 및 저장
        post = Post.builder().user(user).content("테스트 포스트").build();
        postRepository.save(post);
    }

    @Test
    @DisplayName("이미지 저장, 조회 테스트")
    public void createAndGetImage() {
        // given
        // 이미지 생성 및 저장
        Image image = Image.builder().post(post).imageUrl("test-url").build();
        imageRepository.save(image);

        // when
        // 이미지 조회
        Image foundImage = imageRepository.findById(image.getId()).orElse(null);

        // then
        assertThat(foundImage).isNotNull();
        assertThat(foundImage.getImageUrl()).isEqualTo("test-url");
    }

    @Test
    @DisplayName("이미지 삭제 테스트")
    public void deleteImage() {
        // given
        // 이미지 생성 및 저장
        Image image = Image.builder().post(post).imageUrl("test-url").build();
        imageRepository.save(image);

        // when
        // 이미지 삭제
        imageRepository.delete(image);

        // then
        assertThat(imageRepository.findById(image.getId())).isEmpty();
    }
}
