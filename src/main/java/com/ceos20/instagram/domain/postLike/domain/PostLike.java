package com.ceos20.instagram.domain.postLike.domain;

import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.domain.post.domain.Post;
import com.ceos20.instagram.domain.user.domain.User; // User import 추가
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostLike extends BaseTimeEntity {
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

    @Builder
    public PostLike(Long id, Post post, User user) {
        this.id = id;
        this.post = post;
        this.user = user; // User 정보 설정
    }
}

