package com.ceos20.instagram.message.dto;

import com.ceos20.instagram.chatRoom.domain.ChatRoom;
import com.ceos20.instagram.message.domain.Message;
import com.ceos20.instagram.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequestDto {
    private Long userId; // 메시지를 작성한 사용자 고유 번호
    private Long chatRoomId; // 메시지가 속한 채팅방 고유 번호
    private Long parentMessageId; // 답장할 메시지 고유 번호
    private String content;
    private String reaction; // 이모지 반응
    private String fileUrl; // 첨부파일 경로

    @Builder
    public MessageRequestDto(Long userId, Long chatRoomId, Long parentMessageId, String content, String reaction, String fileUrl) {
        this.userId = userId;
        this.chatRoomId = chatRoomId;
        this.parentMessageId = parentMessageId;
        this.content = content;
        this.reaction = reaction;
        this.fileUrl = fileUrl;
    }
    public Message toEntity(User user, Message parentMessage, ChatRoom chatRoom) {
        return Message.builder()
                .user(user)
                .chatRoom(chatRoom) // ChatRoom 설정
                .parentMessage(parentMessage) // 부모 메시지 설정
                .content(content)
                .reaction(reaction)
                .fileUrl(fileUrl)
                .build();
    }
}
