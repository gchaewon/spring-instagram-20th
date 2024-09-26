package com.ceos20.instagram.commentLike.repository;

import com.ceos20.instagram.commentLike.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
