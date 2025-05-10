package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface PostService {

    String createPost(MultipartFile media, String text, String token);
}
