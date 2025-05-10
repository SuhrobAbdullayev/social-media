package com.example.demo.service;

import com.example.demo.domain.dto.request_dto.PostRequestDto;

public interface PostService {

    void createPost(PostRequestDto postRequestDto);
}
