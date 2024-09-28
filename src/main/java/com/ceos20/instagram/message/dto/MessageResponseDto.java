package com.ceos20.instagram.message.dto;

import com.ceos20.instagram.message.domain.Message;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MessageResponseDto {
    private Long id;
    private Long userId; // 작성자의 ID
    private Long parentMessageId; // 부모 메시지의 ID (있다면)
    private String content;
    private String reaction;
    private String fileUrl;
    private LocalDateTime createdAt;

    @Builder
    public MessageResponseDto(Long id, Long userId, Long parentMessageId, String content, String reaction, String fileUrl, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.parentMessageId = parentMessageId;
        this.content = content;
        this.reaction = reaction;
        this.fileUrl = fileUrl;
        this.createdAt = createdAt;
    }

    public static MessageResponseDto from(Message message) {
        return MessageResponseDto.builder()
                .id(message.getId())
                .userId(message.getUser().getId())
                .content(message.getContent())
                .reaction(message.getReaction())
                .fileUrl(message.getFileUrl())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
