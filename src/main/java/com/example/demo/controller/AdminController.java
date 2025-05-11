package com.example.demo.controller;

import com.example.demo.domain.entity.User;
import com.example.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/block")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<String> blockUser(@RequestParam Long userId) {
        adminService.blockUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("Foydalanuvchi bloklandi");
    }

    @PostMapping("/unblock")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<String> unblockUser(@RequestParam Long userId) {
        adminService.unblockUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("Foydalanuvchi blokdan chiqarildi");
    }

    @PostMapping("/block-post")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<String> blockPost(@RequestParam Long postId) {
        adminService.blockPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body("Post bloklandi");
    }

    @PostMapping("/unblock-post")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<String> unblockPost(@RequestParam Long postId) {
        adminService.unblockPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body("Post blokdan chiqarildi");
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllUsers());
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<String> getStatistics() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.Statistics());
    }

}
