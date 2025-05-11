package com.example.demo.service;

import com.example.demo.domain.dto.response_dto.PostResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface PostService {

    String createPost(MultipartFile media, String text, String token);

    PostResponseDto getPostById(Long id);

    List<PostResponseDto> getAllUserPosts(String token);

    PostResponseDto editPost(Long id, String text, MultipartFile media, String token);

    InputStream downloadFile(String key) throws Exception;

    void deletePost(Long id, String token);

    void likePost(Long id, String token);

    void sharePost(Long id, String token);
}
