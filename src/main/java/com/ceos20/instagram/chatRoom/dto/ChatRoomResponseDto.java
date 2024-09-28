package com.ceos20.instagram.chatRoom.dto;

import com.ceos20.instagram.chatRoom.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {
    private Long id;
    private LocalDateTime createdAt;

    @Builder
    public ChatRoomResponseDto(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    // ChatRoom 엔티티를 DTO로 변환하는 메서드
    public static ChatRoomResponseDto from(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
                .id(chatRoom.getId())
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}
