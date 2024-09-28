package com.ceos20.instagram.image.repository;

import com.ceos20.instagram.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Long> findByPostId(Long postId); // 특정 PostId에 속하는 ImageList
}
