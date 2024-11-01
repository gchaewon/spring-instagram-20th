package com.ceos20.instagram.domain.comment.repository;

import com.ceos20.instagram.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 포스트의 댓글 전체 리스트를 조회하는 메서드
    List<Comment> findByPostId(Long postId);
    // 특정 부모 댓글의 대댓글을 조회하는 메서드
    List<Comment> findByParentCommentId(Long parentId);
}
