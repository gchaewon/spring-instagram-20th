package com.ceos20.instagram.domain.post.repository;

import com.ceos20.instagram.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p JOIN FETCH p.user")
    List<Post> findAllWithUsers();

    List<Post> findByUserId(Long userId); // 유저의 포스트 전체 조회
}
