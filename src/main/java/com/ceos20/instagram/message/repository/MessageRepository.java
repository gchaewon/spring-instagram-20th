package com.ceos20.instagram.message.repository;

import com.ceos20.instagram.message.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
