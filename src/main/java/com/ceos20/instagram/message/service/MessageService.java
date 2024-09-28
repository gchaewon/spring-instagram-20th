package com.ceos20.instagram.message.service;

import com.ceos20.instagram.chatRoom.domain.ChatRoom;
import com.ceos20.instagram.chatRoom.repository.ChatRoomRepository;
import com.ceos20.instagram.message.domain.Message;
import com.ceos20.instagram.message.dto.MessageRequestDto;
import com.ceos20.instagram.message.dto.MessageResponseDto;
import com.ceos20.instagram.message.repository.MessageRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository, ChatRoomRepository chatRoomRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    // 메시지 생성 메서드
    @Transactional
    public MessageResponseDto createMessage(MessageRequestDto requestDto, Long userId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new  IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(requestDto.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + requestDto.getChatRoomId()));

        // 부모 메시지 조회 (해당 메시지의 부모가 존재하는 경우)
        Message parentMessage = null;
        if (requestDto.getParentMessageId() != null) {
            parentMessage = messageRepository.findById(requestDto.getParentMessageId())
                    .orElseThrow(() -> new  IllegalArgumentException("답장할 메시지를 찾을 수 없습니다: " +requestDto.getParentMessageId()));
        }

        // 메시지 엔티티 생성
        Message message = requestDto.toEntity(user, parentMessage, chatRoom);

        // 메시지 저장
        Message savedMessage = messageRepository.save(message);

        // ResponseDto 변환 및 반환
        return MessageResponseDto.from(savedMessage);
    }

    // 메시지 조회 메서드
    @Transactional(readOnly = true)
    public MessageResponseDto getMessageById(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다: " + messageId));
        return MessageResponseDto.from(message);
    }

    // 특정 채팅방의 모든 메시지 조회 메서드
    @Transactional(readOnly = true)
    public List<MessageResponseDto> getMessagesByChatRoomId(Long chatRoomId) {
        List<Message> messages = messageRepository.findAllByChatRoomId(chatRoomId);
        return messages.stream()
                .map(MessageResponseDto::from)
                .collect(Collectors.toList());
    }

}
