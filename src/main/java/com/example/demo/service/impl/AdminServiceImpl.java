package com.example.demo.service.impl;

import com.example.demo.domain.entity.Post;
import com.example.demo.domain.entity.User;
import com.example.demo.exceptions.PostException;
import com.example.demo.exceptions.UserException;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public void blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("Bunday foydalanuvchi topilmadi"));
        user.setIsBlocked(true);
        userRepository.save(user);
    }

    @Override
    public void unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("Bunday foydalanuvchi topilmadi"));
        user.setIsBlocked(false);
        userRepository.save(user);
    }

    @Override
    public void blockPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Bunday post topilmadi"));

        post.setBlocked(true);
        postRepository.save(post);
    }

    @Override
    public void unblockPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Bunday post topilmadi"));

        post.setBlocked(false);
        postRepository.save(post);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserException("Foydalanuvchilar topilmadi");
        }
        return users;
    }

    @Override
    public String Statistics() {
        Long userCount = userRepository.count();
        Long postCount = postRepository.count();
        return "Umumiy foydalanuvchilar soni: " + userCount + "\n Postlar soni: " + postCount;
    }

}
