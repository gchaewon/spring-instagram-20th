package com.ceos20.instagram.domain.follow.repository;

import com.ceos20.instagram.domain.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 팔로워 목록 조회 메서드
    @Query("SELECT f FROM Follow f WHERE f.following.id = :userId")
    List<Follow> findAllFollwerByUserId(@Param("userId") Long userId);

    // 팔로잉 목록 조회 메서드
    @Query("SELECT f FROM Follow f WHERE f.follower.id = :userId")
    List<Follow> findAllFollowingByUserId(@Param("userId")Long userId);
    // 팔로우 관계 찾기 메서드
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
