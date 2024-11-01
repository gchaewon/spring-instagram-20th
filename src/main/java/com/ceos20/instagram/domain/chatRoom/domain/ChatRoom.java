package com.ceos20.instagram.domain.chatRoom.domain;

import com.ceos20.instagram.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_room_id")
    private Long id;

    @Builder
    public ChatRoom(Long id){
        this.id = id;
    }
}
