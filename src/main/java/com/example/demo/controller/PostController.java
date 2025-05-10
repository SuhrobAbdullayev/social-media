package com.example.demo.controller;

import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('read')")
    public ResponseEntity<?> createPost(
            @RequestPart(value = "media", required = false) MultipartFile media,
            @RequestPart("text") String text,
            @RequestHeader("Authorization") String token
    ) {
            String result = postService.createPost(media, text, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

}
