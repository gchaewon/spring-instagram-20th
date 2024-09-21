package com.ceos20.instagram.chatParticipant.repository;

import com.ceos20.instagram.chatParticipant.domain.ChatParticipant;
import com.ceos20.instagram.chatRoom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
}
