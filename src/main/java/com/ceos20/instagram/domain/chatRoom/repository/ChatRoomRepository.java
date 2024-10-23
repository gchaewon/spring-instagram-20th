package com.ceos20.instagram.domain.chatRoom.repository;


import com.ceos20.instagram.domain.chatRoom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
