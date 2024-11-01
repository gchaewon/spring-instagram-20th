package com.ceos20.instagram.domain.commentLike.repository;

import com.ceos20.instagram.domain.commentLike.domain.CommentLike;
import com.ceos20.instagram.domain.postLike.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    // 특정 댓글에 대한 특정 사용자의 좋아요를 찾는 메서드
    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);

    // 댓글에 대한 좋아요 리스트를 조회하는 메서드
    List<CommentLike> findByCommentId(Long commentId);
}
