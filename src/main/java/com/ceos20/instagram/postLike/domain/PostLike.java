package com.ceos20.instagram.postLike.domain;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.user.domain.User; // User import 추가
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class) // @CreatedDate를 위한 어노테이션
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public PostLike(Long id, Post post, User user, LocalDateTime createdAt) {
        this.id = id;
        this.post = post;
        this.user = user; // User 정보 설정
        this.createdAt = createdAt;
    }
}

