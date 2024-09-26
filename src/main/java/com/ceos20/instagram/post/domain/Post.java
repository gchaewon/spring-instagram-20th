package com.ceos20.instagram.post.domain;

import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class) // @CreatedDate를 위한 어노테이션
public class Post {
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

   @CreatedDate
   private LocalDateTime createdAt;
   @LastModifiedDate
   private LocalDateTime modifiedAt;

    @Enumerated(EnumType.STRING)
    private CommentOption commentOption = CommentOption.ENABLED; // 기본값 설정

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
