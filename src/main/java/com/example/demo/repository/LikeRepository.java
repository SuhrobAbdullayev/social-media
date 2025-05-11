package com.example.demo.repository;

import com.example.demo.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findAllByPostId(Long postId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);
}