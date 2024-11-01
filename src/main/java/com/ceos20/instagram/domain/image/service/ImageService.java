package com.ceos20.instagram.domain.image.service;

import com.ceos20.instagram.domain.image.domain.Image;
import com.ceos20.instagram.domain.image.repository.ImageRepository;
import com.ceos20.instagram.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class ImageService {
    private final ImageRepository imageRepository;

    // 특정 포스트의 이미지 생성 메서드
    @Transactional
    public List<Image> createImages(List<String> imageUrls, Post post) {
        List<Image> images = imageUrls.stream()
                .map(url -> Image.createImage(url, post))
                .collect(Collectors.toList());

        // 이미지 저장
        return imageRepository.saveAll(images);
    }

    // 특정 포스트의 이미지 전체 조회 메서드
    public List<Image> getImagesByPostId(Long postId) {
        return imageRepository.findByPostId(postId);
    }

    // 여러 포스트의 이미지 전체 조회 메서드
    public List<List<Image>> getImagesByPostIds(List<Long> postIds){
        return imageRepository.findByPostIdIn(postIds);
    }

    // 특정 포스트의 이미지 전체 삭제 메서드
    public void deleteImagesByPostId(Long postId) {
        List<Image> images = imageRepository.findByPostId(postId);
        if (!images.isEmpty()) {
            imageRepository.deleteAll(images);
        }
    }

    // 이미지 삭제 메서드
    @Transactional
    public void deleteImagesByIds(List<Long> imageIds) {
        for (Long id : imageIds) {
            imageRepository.findById(id).orElseThrow(() ->
                    new IllegalArgumentException("이미지 ID " + id + "에 해당하는 이미지가 존재하지 않습니다.")
            );
            imageRepository.deleteById(id); // 각 ID로 이미지를 삭제
        }
    }


}
