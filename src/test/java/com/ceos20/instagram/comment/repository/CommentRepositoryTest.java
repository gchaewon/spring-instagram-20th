package com.ceos20.instagram.comment.repository;

import com.ceos20.instagram.comment.domain.Comment;
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
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private Post post;
    private User user;

    @BeforeEach
    void setUp() {
        // 테스트용 유저 생성 및 저장
        user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .password("password")
                .build();
        userRepository.save(user);

        // 테스트용 게시글 생성 및 저장
        post = Post.builder().user(user).content("테스트 포스트").build();
        postRepository.save(post);
    }

    @Test
    @DisplayName("댓글 저장, 조회 테스트")
    public void createAndGetComment() {
        // given
        // 댓글 생성 및 저장
        Comment comment = Comment.builder().post(post).user(user).content("테스트 댓글").build();
        commentRepository.save(comment);

        // when
        // 댓글 조회
        Comment foundComment = commentRepository.findById(comment.getId()).orElse(null);

        // then
        // 댓글 검증
        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getContent()).isEqualTo("테스트 댓글");
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    public void deleteComment() {
        // given
        // 댓글 생성 및 저장
        Comment comment = Comment.builder().post(post).user(user).content("테스트 댓글").build();
        commentRepository.save(comment);

        // when
        // 댓글 삭제
        commentRepository.delete(comment);

        // then
        // 댓글 삭제 확인
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }
}
