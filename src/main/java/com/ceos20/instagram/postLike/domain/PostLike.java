package com.ceos20.instagram.postLike.domain;

import com.ceos20.instagram.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class) // @CreatedDate를 위한 어노테이션
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    private String imageUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public PostLike(Long id, Post post, String imageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.post = post;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }
}
