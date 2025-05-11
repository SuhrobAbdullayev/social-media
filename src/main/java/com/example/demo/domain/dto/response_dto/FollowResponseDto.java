package com.example.demo.domain.dto.response_dto;

import java.time.LocalDateTime;

public record FollowResponseDto(
        Long userId,
        String name,
        String username,
        LocalDateTime dateTime
) {
}
