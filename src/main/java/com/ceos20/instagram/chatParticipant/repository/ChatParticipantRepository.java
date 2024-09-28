package com.ceos20.instagram.chatParticipant.repository;

import com.ceos20.instagram.chatParticipant.domain.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    // 특정 유저의 참가 채팅 목록 조회
    List<ChatParticipant> findAllByUserId(Long userId);

    // 특정 채팅방의 참가자 목록 조회
    List<ChatParticipant> findAllByChatRoomId(Long chatRoomId);
}
