package com.ceos20.instagram.message.domain;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.chatRoom.domain.ChatRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class) // @CreatedDate를 위한 어노테이션
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_message_id")
    private Message parentMessage; // 특정 메시지에 답장 하는 경우, 해당 메시지

    @Column(length = 10000)
    private String content; // 첨부 파일만 가는 경우, 내용 없어도 됨

    private String reaction; // 메시지에 대한 공감 이모지

    private String fileUrl; // 첨부파일이 있는 경우, 파일의 경로

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Message(Long id, ChatRoom chatRoom, User user, Message parentMessage, String content, String reaction, String fileUrl, LocalDateTime createdAt) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.user = user;
        this.parentMessage = parentMessage;
        this.content = content;
        this.reaction = reaction;
        this.fileUrl = fileUrl;
        this.createdAt = createdAt;
    }
}
