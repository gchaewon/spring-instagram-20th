package com.ceos20.instagram.chatParticipant.repository;

import com.ceos20.instagram.chatParticipant.domain.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
}
