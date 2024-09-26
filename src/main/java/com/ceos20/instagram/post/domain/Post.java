package com.ceos20.instagram.post.domain;

import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

   private String content;

   private LocalDateTime createdAt;
   private LocalDateTime modifiedAt;

    @Enumerated(EnumType.STRING)
    private CommentOption commentOption;

    @Builder
    public Post(Long id, User user, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, CommentOption commentOption) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.commentOption = commentOption;
    }


}
