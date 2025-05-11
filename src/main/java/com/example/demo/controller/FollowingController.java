package com.example.demo.controller;

import com.example.demo.domain.dto.response_dto.FollowResponseDto;
import com.example.demo.service.FollowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/interaction")
@RequiredArgsConstructor
public class FollowingController {

    private final FollowingService followingService;

    @PostMapping("/follow")
    @PreAuthorize("hasAnyAuthority('read')")
    public ResponseEntity<String> followUser(@RequestParam("userId") Long userId,
                                     @RequestHeader("Authorization") String token) {
        followingService.followUser(userId, token);
        return ResponseEntity.status(HttpStatus.OK).body("Siz foydalanuvchiga follow bosdingiz");
    }

    @PostMapping("/unfollow")
    @PreAuthorize("hasAnyAuthority('read')")
    public ResponseEntity<String> unfollowUser(@RequestParam("userId") Long userId,
                                       @RequestHeader("Authorization") String token) {
        followingService.unfollowUser(userId, token);
        return ResponseEntity.status(HttpStatus.OK).body("Siz foydalanuvchiga unfollow bosdingiz");
    }

    @GetMapping("/followers")
    @PreAuthorize("hasAnyAuthority('read')")
    public ResponseEntity<List<FollowResponseDto>> getFollowers(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(followingService.getFollowers(token));
    }

    @GetMapping("/followings")
    @PreAuthorize("hasAnyAuthority('read')")
    public ResponseEntity<List<FollowResponseDto>> getFollowing(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(followingService.getFollowing(token));
    }
}
