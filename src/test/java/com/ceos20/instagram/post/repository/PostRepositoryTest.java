package com.ceos20.instagram.post.repository;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.CommentOption;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
// 테스트용 내장 DB로 변경하지 않기 위한 어노테이션
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    // 테스트 시작 전 쌓인 DB를 지우기
    @BeforeEach
    void cleanUp() {
        postRepository.deleteAll();
    }

    @Test
    @Transactional
    @Rollback(false) // mysql에 실제로 들어가는지 확인하기 위한 어노테이션
    @DisplayName("Post 저장 테스트")
    void savePost(){
        //given
        // 사용자 생성 및 저장
        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);

        // 게시물 3개 생성
        for (int i = 1; i <= 3; i++) {
            Post post = new Post();
            post.setContent("Test Content " + i);
            post.setUser(user);
            post.setCreated_time(LocalDateTime.now());
            post.setEdited_time(LocalDateTime.now());
            post.setComment_option(CommentOption.ENABLED);
            postRepository.save(post);
        }

        // when
        // 저장된 게시물 전부 조회
        List<Post> posts = postRepository.findAll();

        // then
        // 저장된 게시물 개수 검증
        assertThat(posts).hasSize(3);
        // 반복문으로 내용 검증 및 출력
        for (int i = 0; i < posts.size(); i++) {
            assertThat(posts.get(i).getContent()).isEqualTo("Test Content " + (i + 1));
            System.out.println(posts.get(i).getContent());
        }
    }

}