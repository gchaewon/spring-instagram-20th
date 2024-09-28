package com.ceos20.instagram.post.repository;

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
    private User user2;
    private Post post1;
    private Post post2;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성
        user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .password("password")
                .build();
        user2 = User.builder()
                .email("test2@example.com")
                .username("testuser2")
                .password("password")
                .build();

        userRepository.save(user);
        userRepository.save(user2);

        // 테스트용 포스트 생성
        post1 = Post.builder()
                .user(user)
                .content("포스트 1")
                .commentOption(CommentOption.ENABLED)
                .build();
        post2 = Post.builder()
                .user(user2)
                .content("포스트 2")
                .commentOption(CommentOption.ENABLED)
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
    }

    @Test
    @DisplayName("N+1 문제 테스트")
    public void nPlusOneProblemTest() {
        // when
        List<Post> posts = postRepository.findAll();

        // 이 시점에서 User에 접근하여 N+1 문제를 유발
        for (Post post : posts) {
            String userEmail = post.getUser().getEmail(); // 이 부분에서 User 정보를 가져옴
            System.out.println("User Email: " + userEmail); // User 정보를 출력
        }

        // then
        assertThat(posts).hasSize(2);
    }

    @Test
    @DisplayName("N+1 문제 해결 테스트")
    public void nPlusOneSolveTest() {
        // when
        List<Post> posts = postRepository.findAllWithUsers();

        // 이 시점에서 User에 접근하여 N+1 문제를 유발
        for (Post post : posts) {
            String userEmail = post.getUser().getEmail(); // 이 부분에서 User 정보를 가져옴
            System.out.println("User Email: " + userEmail); // User 정보를 출력
        }

        // then
        assertThat(posts).hasSize(2);
    }
}
