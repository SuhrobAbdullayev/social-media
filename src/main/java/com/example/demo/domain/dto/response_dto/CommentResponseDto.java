package com.example.demo.domain.dto.response_dto;

public record CommentResponseDto(
        Long id,
        String text,
        CommentUserResponseDto user,
        Integer likes
) {
}
