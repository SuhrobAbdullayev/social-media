package com.example.demo.repository;

import com.example.demo.domain.entity.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    Optional<Object> findByUserIdAndFollowerId(Long userId, Long followerId);

    List<Follower> findAllByUserId(Long userId);

    List<Follower> findAllByFollowerId(Long followerId);

    boolean existsByUserIdAndFollowerId(Long userId, Long followerId);
}