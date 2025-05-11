package com.example.demo.controller;

import com.example.demo.domain.dto.request_dto.CommentRequestDto;
import com.example.demo.domain.dto.response_dto.CommentResponseDto;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('read')")
    public ResponseEntity<String> createComment(@RequestBody CommentRequestDto commentRequestDto,
                                        @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(commentRequestDto, token));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('read')")
    public ResponseEntity<List<CommentResponseDto>> getAllCommentsByPostId(@RequestParam("postId") Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsByPostId(postId));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('read')")
    public ResponseEntity<String> deleteComment(@RequestParam("commentId") Long commentId,
                                        @RequestHeader("Authorization") String token) {
        commentService.deleteComment(commentId, token);
        return ResponseEntity.status(HttpStatus.OK).body("Comment o`chirildi");
    }

}
