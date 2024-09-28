package com.ceos20.instagram.postlike.repository;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.postLike.domain.PostLike;
import com.ceos20.instagram.postLike.repository.PostLikeRepository;
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
public class PostLikeRepositoryTest {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private Post post;
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

        // 테스트용 게시글 생성
        post = Post.builder().user(user).content("테스트 포스트").build();
        postRepository.save(post);
    }

    @Test
    @DisplayName("게시글 좋아요 저장, 조회 테스트")
    public void createAndGetPostLike() {
        // given
        // 게시글 좋아요 생성 및 저장
        PostLike postLike = PostLike.builder().post(post).build();
        postLikeRepository.save(postLike);

        // when
        // 게시글 좋아요 조회
        PostLike foundPostLike = postLikeRepository.findById(postLike.getId()).orElse(null);

        // then
        // 게시글 좋아요 생성 확인
        assertThat(foundPostLike).isNotNull();
    }

    @Test
    @DisplayName("게시글 좋아요 삭제 테스트")
    public void deletePostLike() {
        // given
        // 게시글 좋아요 생성 및 저장
        PostLike postLike = PostLike.builder().post(post).build();
        postLikeRepository.save(postLike);

        // when
        // 게시글 좋아요 삭제
        postLikeRepository.delete(postLike);

        // then
        // 게시글 좋아요 삭제 확인
        assertThat(postLikeRepository.findById(postLike.getId())).isEmpty();
    }
}
