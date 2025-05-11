package com.example.demo.domain.dto.response_dto;

import lombok.Builder;

@Builder
public record PostResponseDto(
        Long id,
        String key,
        String text,
        Boolean isMedia,
        Boolean isBlocked
) {
}
