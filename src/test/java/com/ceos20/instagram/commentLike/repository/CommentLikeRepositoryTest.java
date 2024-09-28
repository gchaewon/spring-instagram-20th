package com.ceos20.instagram.commentLike.repository;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.commentLike.domain.CommentLike;
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
public class CommentLikeRepositoryTest {
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private Post post;
    private User user;
    private Comment comment;

    @BeforeEach
    void setUp() {
        // 테스트용 유저 생성 및 저장
        user = User.builder().email("test@example.com").username("testuser").password("password").build();
        userRepository.save(user);

        // 테스트용 게시글 생성 및 저장
        post = Post.builder().user(user).content("테스트 포스트").build();
        postRepository.save(post);

        // 테스트용 댓글 생성 및 저장
        comment = Comment.builder().post(post).user(user).content("테스트 댓글").build();
        commentRepository.save(comment);
    }

    @Test
    @DisplayName("댓글 좋아요 저장, 조회 테스트")
    public void createAndGetCommentLike() {
        // given
        // 댓글 좋아요 생성 및 저장
        CommentLike commentLike = CommentLike.builder().comment(comment).build();
        commentLikeRepository.save(commentLike);

        // when
        // 댓글 좋아요 조회
        CommentLike foundCommentLike = commentLikeRepository.findById(commentLike.getId()).orElse(null);

        // then
        assertThat(foundCommentLike).isNotNull();
    }

    @Test
    @DisplayName("댓글 좋아요 삭제 테스트")
    public void deleteCommentLike() {
        // given
        // 댓글 좋아요 생성 및 저장
        CommentLike commentLike = CommentLike.builder().comment(comment).build();
        commentLikeRepository.save(commentLike);

        // when
        // 댓글 좋아요 삭제
        commentLikeRepository.delete(commentLike);

        // then
        // 댓글 좋아요 삭제 검증
        assertThat(commentLikeRepository.findById(commentLike.getId())).isEmpty();
    }
}
