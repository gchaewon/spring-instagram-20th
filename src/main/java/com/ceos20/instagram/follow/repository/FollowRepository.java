package com.ceos20.instagram.follow.repository;

import com.ceos20.instagram.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 팔로워 목록 조회 메서드
    List<Follow> findAllFollwerByUserId(Long userId);
    // 팔로잉 목록 조회 메서드
    List<Follow> findAllFollowingByUserId(Long userId);
    // 팔로우 관계 찾기 메서드
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
