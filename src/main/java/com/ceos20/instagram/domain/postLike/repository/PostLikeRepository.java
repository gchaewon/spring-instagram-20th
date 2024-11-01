package com.ceos20.instagram.domain.postLike.repository;

import com.ceos20.instagram.domain.postLike.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    // 특정 게시물에 대한 특정 사용자의 좋아요를 찾는 메서드
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);
}
