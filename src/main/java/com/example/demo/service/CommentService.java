package com.example.demo.service;

import com.example.demo.domain.dto.request_dto.CommentRequestDto;
import com.example.demo.domain.dto.response_dto.CommentResponseDto;

import java.util.List;

public interface CommentService {

    String createComment(CommentRequestDto commentRequestDto, String token);

    List<CommentResponseDto> getAllCommentsByPostId(Long postId);

    void deleteComment(Long commentId, String token);
}
