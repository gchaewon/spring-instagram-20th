package com.ceos20.instagram.profile.repository;

import com.ceos20.instagram.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
