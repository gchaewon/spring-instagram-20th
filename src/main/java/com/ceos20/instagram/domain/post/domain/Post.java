package com.ceos20.instagram.domain.post.domain;

import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @NotNull
    @Column(length = 2200)
   private String content;

    @Enumerated(EnumType.STRING)
    private CommentOption commentOption = CommentOption.ENABLED; // 기본값 설정

    @Builder
    public Post(Long id, User user, String content, CommentOption commentOption) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.commentOption = commentOption;
    }

}
