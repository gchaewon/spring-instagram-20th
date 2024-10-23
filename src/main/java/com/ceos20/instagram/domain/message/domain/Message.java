package com.ceos20.instagram.domain.message.domain;

import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.domain.user.domain.User;
import com.ceos20.instagram.domain.chatRoom.domain.ChatRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
public class Message extends BaseTimeEntity {
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

    @Builder
    public Message(Long id, ChatRoom chatRoom, User user, Message parentMessage, String content, String reaction, String fileUrl) {
        this.id = id;
        this.chatRoom = chatRoom;
        this.user = user;
        this.parentMessage = parentMessage;
        this.content = content;
        this.reaction = reaction;
        this.fileUrl = fileUrl;
    }
}
