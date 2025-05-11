package com.example.demo.service.impl;

import com.example.demo.domain.dto.response_dto.FollowResponseDto;
import com.example.demo.domain.entity.Follower;
import com.example.demo.domain.entity.User;
import com.example.demo.exceptions.UserException;
import com.example.demo.repository.FollowerRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FollowingService;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowingServiceImpl implements FollowingService {

    private final PostService postService;
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    @Override
    public void followUser(Long userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("Foydalanuvchi topilmadi"));
        User follower = postService.getUser(token);

        if (user.getId().equals(follower.getId())) {
            throw new UserException("O`ziga o`zi follow bosa olmaydi");
        }
        if (followerRepository.existsByUserIdAndFollowerId(user.getId(), follower.getId())) {
            throw new UserException("Siz bu foydalanuvchiga allaqachon follow bosgansiz");
        }

        Follower followerEntity = new Follower();
        followerEntity.setUserId(user.getId());
        followerEntity.setFollowerId(follower.getId());
        followerRepository.save(followerEntity);
    }

    @Override
    public void unfollowUser(Long userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("Foydalanuvchi topilmadi"));
        User follower = postService.getUser(token);

        if (user.getId().equals(follower.getId())) {
            throw new UserException("O`ziga o`zi unfollow bo`la olmaydi");
        }

        Follower followerEntity = (Follower) followerRepository.findByUserIdAndFollowerId(user.getId(), follower.getId())
                .orElseThrow(() -> new UserException("Siz bu foydalanuvchiga follow qilmagansiz"));
        followerRepository.delete(followerEntity);
    }

    @Override
    public List<FollowResponseDto> getFollowers(String token) {
        User user = postService.getUser(token);
        List<Follower> followers = followerRepository.findAllByUserId(user.getId());
        return followers.stream()
                .map(follower -> new FollowResponseDto(
                        follower.getFollowerId(),
                        userRepository.findById(follower.getFollowerId()).get().getFirstName(),
                        userRepository.findById(follower.getFollowerId()).get().getUsername(),
                        follower.getDateTime()
                ))
                .toList();
    }

    @Override
    public List<FollowResponseDto> getFollowing(String token) {
        User user = postService.getUser(token);
        List<Follower> followees = followerRepository.findAllByFollowerId(user.getId());
        return followees.stream()
                .map(followee -> new FollowResponseDto(
                        followee.getUserId(),
                        userRepository.findById(followee.getUserId()).get().getFirstName(),
                        userRepository.findById(followee.getUserId()).get().getUsername(),
                        followee.getDateTime()
                ))
                .toList();
    }


}
