package com.example.demo.service.impl;

import com.example.demo.domain.dto.request_dto.PostRequestDto;
import com.example.demo.domain.entity.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public void createPost(PostRequestDto postRequestDto) {
    }

}
