package com.example.demo.service;

import com.example.demo.domain.dto.response_dto.FollowResponseDto;

import java.util.List;

public interface FollowingService {
    void followUser(Long userId, String token);

    void unfollowUser(Long userId, String token);

    List<FollowResponseDto> getFollowers(String token);

    List<FollowResponseDto> getFollowing(String token);
}
