package com.ceos20.instagram.image.repository;

import com.ceos20.instagram.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
