package com.ceos20.instagram.domain.comment.domain;

import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.domain.post.domain.Post;
import com.ceos20.instagram.domain.user.domain.User;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity{
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

    @NotNull
    @Column(length = 2200)
    private String content;

    @Builder
    public Comment(User user, Post post, Comment parentComment, String content) {
        this.user = user;
        this.post = post;
        this.parentComment = parentComment;
        this.content = content;
    }
}