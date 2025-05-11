package com.example.demo.domain.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentRequestDto {
    @JsonProperty("post_id") Long postId;
    String text;
}
