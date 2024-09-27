package com.ceos20.instagram.user.repository;

import com.ceos20.instagram.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
