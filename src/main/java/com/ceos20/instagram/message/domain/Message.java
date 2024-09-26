package com.ceos20.instagram.message.domain;

import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_message_id")
    private Message parentMessage;

    private String content;

    private String reaction;

    private String fileUrl;

    private LocalDateTime createdAt;

    @Builder
    public Message(Long id, User user, Message parentMessage, String content, String reaction, String fileUrl, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.parentMessage = parentMessage;
        this.content = content;
        this.reaction = reaction;
        this.fileUrl = fileUrl;
        this.createdAt = createdAt;
    }
}
