package com.example.demo.domain.dto.request_dto;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public record PostRequestDto(
        @RequestPart("media") MultipartFile media,
        @RequestPart("text") String text
) {
}
