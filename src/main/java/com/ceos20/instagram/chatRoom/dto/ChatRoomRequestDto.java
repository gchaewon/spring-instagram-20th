package com.ceos20.instagram.chatRoom.dto;

import com.ceos20.instagram.chatRoom.domain.ChatRoom;
import lombok.Builder;
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
