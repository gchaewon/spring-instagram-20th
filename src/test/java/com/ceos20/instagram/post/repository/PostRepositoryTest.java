package com.ceos20.instagram.post.repository;

import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.post.domain.CommentOption;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성
        user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .password("password")
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("post 저장, 조회 테스트")
    public void createAndGetPost() {
        // given
        // 포스트 생성
        Post post = Post.builder()
                .user(user)
                .content("테스트 포스트")
                .commentOption(CommentOption.ENABLED)
                .build();

        postRepository.save(post);

        // when
        Post foundPost = postRepository.findById(post.getId()).orElse(null);

        // then
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getContent()).isEqualTo("테스트 포스트");
    }

    @Test
    @DisplayName("post 조회 테스트")
    public void findAllPosts() {
        // given
        Post post1 = Post.builder().user(user).content("포스트 1").commentOption(CommentOption.ENABLED).build();
        Post post2 = Post.builder().user(user).content("포스트 2").commentOption(CommentOption.ENABLED).build();
        postRepository.save(post1);
        postRepository.save(post2);

        // when
        List<Post> posts = postRepository.findAll();

        // then
        assertThat(posts).hasSize(2);
    }

    @Test
    @DisplayName("post 삭제 테스트")
    public void deletePost() {
        // given
        Post post = Post.builder().user(user).content("포스트 삭제 테스트").commentOption(CommentOption.ENABLED).build();
        postRepository.save(post);

        // when
        postRepository.delete(post);

        // then
        assertThat(postRepository.findById(post.getId())).isEmpty();
    }
}
