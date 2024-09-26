package com.ceos20.instagram.commentLike.domain;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class) // @CreatedDate를 위한 어노테이션
public class CommentLike {
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

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public CommentLike(Long id, Comment comment, User user, LocalDateTime createdAt){
        this.id = id;
        this.comment = comment;
        this.user = user;
        this.createdAt = createdAt;
    }

}
