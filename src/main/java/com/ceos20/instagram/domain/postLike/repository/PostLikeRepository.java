package com.ceos20.instagram.domain.postLike.repository;

import com.ceos20.instagram.domain.postLike.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
