package com.ceos20.instagram.domain.chatParticipant.dto;

import com.ceos20.instagram.domain.chatParticipant.domain.ChatParticipant;
import com.ceos20.instagram.domain.chatRoom.domain.ChatRoom;
import com.ceos20.instagram.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatParticipantRequestDto {
    private Long userId; // 참가할 유저 ID
    private Long chatRoomId; // 참가할 채팅방 ID

    @Builder
    public ChatParticipantRequestDto(Long userId, Long chatRoomId) {
        this.userId = userId;
        this.chatRoomId = chatRoomId;
    }

    // ChatParticipant 엔티티로 변환하는 메서드
    public ChatParticipant toEntity(User user, ChatRoom chatRoom) {
        return ChatParticipant.builder()
                .user(user)
                .chatRoom(chatRoom)
                .build();
    }
}
