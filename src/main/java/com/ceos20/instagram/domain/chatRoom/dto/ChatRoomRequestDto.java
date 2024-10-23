package com.ceos20.instagram.domain.chatRoom.dto;

import com.ceos20.instagram.domain.chatRoom.domain.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequestDto {
    // ChatRoom 엔티티로 변환하는 메서드
    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .build();
    }

}
