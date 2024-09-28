package com.ceos20.instagram.comment.repository;

import com.ceos20.instagram.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 포스트 ID에 대한 댓글을 조회하는 메서드
    List<Comment> findByPostId(Long postId);
    // 특정 부모 댓글 ID에 대한 대댓글을 조회하는 메서드
    List<Comment> findByParentComment_Id(Long parentId);
}
