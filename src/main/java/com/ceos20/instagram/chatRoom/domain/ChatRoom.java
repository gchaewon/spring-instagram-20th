package com.ceos20.instagram.chatRoom.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class) // @CreatedDate를 위한 어노테이션
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_room_id")
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public ChatRoom(Long id, LocalDateTime createdAt){
        this.id = id;
        this.createdAt = createdAt;
    }
}
