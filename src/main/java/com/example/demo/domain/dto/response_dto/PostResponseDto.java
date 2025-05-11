package com.example.demo.domain.dto.response_dto;

import lombok.Builder;

@Builder
public record PostResponseDto(
        Long id,
        String key,
        String text,
        Integer likeCount,
        Integer viewCount,
        Integer shareCount,
        Boolean isMedia,
        Boolean isBlocked
) {
}
