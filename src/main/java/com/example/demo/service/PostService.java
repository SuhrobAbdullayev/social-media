package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface PostService {

    String createPost(MultipartFile media, String text, String token);

    InputStream downloadFile(String key) throws Exception;
}
