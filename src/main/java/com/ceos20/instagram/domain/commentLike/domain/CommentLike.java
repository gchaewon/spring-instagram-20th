package com.ceos20.instagram.domain.commentLike.domain;

import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.domain.comment.domain.Comment;
import com.ceos20.instagram.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter

public class CommentLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;


    @Builder
    public CommentLike(Long id, Comment comment, User user){
        this.id = id;
        this.comment = comment;
        this.user = user;
    }

}
