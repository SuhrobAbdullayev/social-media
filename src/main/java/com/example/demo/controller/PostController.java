package com.example.demo.controller;

import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('read')")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('read')")
    public ResponseEntity<?> getAllUserPosts(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllUserPosts(token));
    }

    @GetMapping("/download/{key}")
    public ResponseEntity<InputStreamResource> downloadResource(@PathVariable String key) {
        try {
            InputStream inputStream = postService.downloadFile(key);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + key)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('read')")
    public ResponseEntity<?> editPost(
            @PathVariable Long id,
            @RequestPart(value = "media", required = false) MultipartFile media,
            @RequestPart("text") String text,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.editPost(id, text, media, token));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('read')")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        postService.deletePost(id, token);
        return ResponseEntity.status(HttpStatus.OK).body("Post o'chirildi");
    }
}
