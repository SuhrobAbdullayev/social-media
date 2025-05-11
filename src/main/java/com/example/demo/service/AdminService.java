package com.example.demo.service;

import com.example.demo.domain.entity.User;

import java.util.List;

public interface AdminService {

    void blockUser(Long userId);

    void unblockUser(Long userId);

    void blockPost(Long postId);

    void unblockPost(Long postId);

    List<User> getAllUsers();

    String Statistics();

}
