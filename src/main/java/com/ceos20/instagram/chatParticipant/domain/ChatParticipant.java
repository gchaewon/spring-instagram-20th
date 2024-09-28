package com.ceos20.instagram.chatParticipant.domain;

import com.ceos20.instagram.chatRoom.domain.ChatRoom;
import com.ceos20.instagram.user.domain.User;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    public ChatParticipant(Long id, User user, ChatRoom chatRoom){
        this.id = id;
        this.user = user;
        this.chatRoom = chatRoom;
    }
}
