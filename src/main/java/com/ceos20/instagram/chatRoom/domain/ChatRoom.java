package com.ceos20.instagram.chatRoom.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_room_id")
    private Long id;

    private LocalDateTime createdAt;

    @Builder
    public ChatRoom(Long id, LocalDateTime createdAt){
        this.id = id;
        this.createdAt = createdAt;
    }
}
