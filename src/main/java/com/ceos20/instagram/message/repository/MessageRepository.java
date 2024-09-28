package com.ceos20.instagram.message.repository;

import com.ceos20.instagram.message.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // 특정 채팅방의 전체 메시지 조회 메서드
    List<Message> findAllByChatRoomId(Long chatRoomId);
}
