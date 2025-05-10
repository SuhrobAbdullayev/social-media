package com.example.demo.domain.dto.request_dto;

import org.springframework.web.multipart.MultipartFile;

public record PostRequestDto(
        MultipartFile media,
        String text
) {
}
