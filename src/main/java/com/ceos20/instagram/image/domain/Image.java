package com.ceos20.instagram.image.domain;

import com.ceos20.instagram.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    private String imageUrl;

    @Builder
    public Image(Long id, Post post, String imageUrl) {
        this.id = id;
        this.post = post;
        this.imageUrl = imageUrl;
    }
}
