package com.ceos20.instagram.chatParticipant.dto;

import com.ceos20.instagram.chatParticipant.domain.ChatParticipant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatParticipantResponseDto {
    private Long id; // 채팅 참가 id
    private Long userId; // 참가할 유저 고유 번호
    private Long chatRoomId; // 참가할 채팅방 고유 번호

    // ChatParticipant 객체를 DTO로 변환하는 메서드
    public static ChatParticipantResponseDto from(ChatParticipant participant) {
        ChatParticipantResponseDto dto = new ChatParticipantResponseDto();
        dto.id = participant.getId();
        dto.userId = participant.getUser().getId();
        dto.chatRoomId = participant.getChatRoom().getId();
        return dto;
    }
}
