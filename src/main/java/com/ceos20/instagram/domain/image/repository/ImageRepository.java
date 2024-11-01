package com.ceos20.instagram.domain.image.repository;

import com.ceos20.instagram.domain.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostId(Long postId); // 특정 PostId에 속하는 ImageList
    List<List<Image>> findByPostIdIn(List<Long> postIds); // 여러 PostId의 ImageList
}
