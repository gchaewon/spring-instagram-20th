package com.ceos20.instagram.chatRoom.service;

import com.ceos20.instagram.chatParticipant.domain.ChatParticipant;
import com.ceos20.instagram.chatParticipant.dto.ChatParticipantResponseDto;
import com.ceos20.instagram.chatParticipant.repository.ChatParticipantRepository;
import com.ceos20.instagram.chatRoom.domain.ChatRoom;
import com.ceos20.instagram.chatRoom.dto.ChatRoomRequestDto;
import com.ceos20.instagram.chatRoom.dto.ChatRoomResponseDto;
import com.ceos20.instagram.chatRoom.repository.ChatRoomRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    // 채팅방 생성 및 참가 메서드
    @Transactional
    public void createAndJoinChatRoom(Long userId) {
        // 새로운 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .createdAt(LocalDateTime.now()) // 생성 시간 설정
                .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // 사용자가 생성한 채팅방에 참가하도록함
        // 회원 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        // 채팅 참가 생성
        ChatParticipant participant = ChatParticipant.builder()
                .user(user)
                .chatRoom(savedChatRoom)
                .build();
        // 채팅 참가 저장
        chatParticipantRepository.save(participant);
    }

    // 특정 유저의 채팅방 리스트 조회 메서드
    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getChatRoomsByUserId(Long userId) {
        // 특정 유저가 참가 상태 조회
        List<ChatParticipant> participants = chatParticipantRepository.findAllByUserId(userId);
        // 특정 유저가 참가하고 있는 채팅 방 리스트를 DTO 리스트로 반환
        return participants.stream()
                .map(participant -> ChatRoomResponseDto.from(participant.getChatRoom()))
                .collect(Collectors.toList());
    }

    // 특정 채팅방의 참가자 리스트 조회 메서드
    @Transactional(readOnly = true)
    public List<ChatParticipantResponseDto> getParticipantsByChatRoomId(Long chatRoomId) {
        // 특정 채팅방에 참가하는 참가자 리스트를 DTO 리스트로 반환
        List<ChatParticipant> participants = chatParticipantRepository.findAllByChatRoomId(chatRoomId);
        return participants.stream()
                .map(ChatParticipantResponseDto::from)
                .collect(Collectors.toList());
    }

}
