package com.ceos20.instagram.comment.domain;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    private String content;

    private LocalTime createdAt;
    private LocalTime modifiedAt;

    @Builder
    public Comment(Long id, User user, Post post, Comment parentComment, String content, LocalTime createdAt, LocalTime modifiedAt){
        this.id = id;
        this.user = user;
        this.post = post;
        this.content = content;
        this.parentComment = parentComment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

}
