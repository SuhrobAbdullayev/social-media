package com.example.demo.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.demo.domain.dto.response_dto.PostResponseDto;
import com.example.demo.domain.entity.Like;
import com.example.demo.domain.entity.Post;
import com.example.demo.domain.entity.User;
import com.example.demo.exceptions.PostException;
import com.example.demo.exceptions.UserException;
import com.example.demo.jwt_utils.JwtTokenProvider;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PostService;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AmazonS3 amazonS3;
    private final LikeRepository likeRepository;

    @Value("${services.s3.bucket-name}")
    private String bucketName;

    @Override
    public User getUser(String token) {
        String rawToken = token.replace("Bearer ", "");
        String username = jwtTokenProvider.getUser(rawToken).toLowerCase();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UserException("Bunday foydalanuvchi mavjud emas");
        }
        return user;
    }

    @Override
    public String createPost(MultipartFile media, String text, String token) {
        User user = getUser(token);
        String key = null;

        if (media != null && !media.isEmpty()) {
            key = UUID.randomUUID().toString();

            try {
                String contentType = media.getContentType();
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(media.getSize());
                metadata.setContentType(contentType);

                if (!amazonS3.doesBucketExistV2(bucketName)) {
                    amazonS3.createBucket(bucketName);
                }

                amazonS3.putObject(new PutObjectRequest(
                        bucketName,
                        key,
                        media.getInputStream(),
                        metadata
                ));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload media to MinIO", e);
            }
        }

        Post.PostBuilder builder = Post.builder()
                .text(text)
                .userId(user.getId());

        if (key != null) {
            builder.key(key).media(true);
        }

        Post post = builder.build();
        postRepository.save(post);
        int postCount = postRepository.getAllByUserId(user.getId()).size();
        String message = (key != null)
                ? "Mediali post yaratildi: " + amazonS3.getUrl(bucketName, key).toString()
                : "Tekstli Post yaratildi";

        if (postCount == 10) {
            message += " 🎉 Tabriklaymiz! Sizning 10 ta postingiz bor!";
        }

        return message;
    }


    @Override
    public InputStream downloadFile(String key) throws Exception {
        try {
            S3Object s3Object = amazonS3.getObject(bucketName, key);
            return s3Object.getObjectContent();
        } catch (Exception e) {
            throw new RuntimeException("Xatolik yuz berdi: " + e.getMessage());
        }
    }

    @Override
    public void deletePost(Long id, String token) {
        User user = getUser(token);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException("Post topilmadi"));

        if (!post.getUserId().equals(user.getId())) {
            throw new PostException("Siz bu postni o'chira olmaysiz");
        }

        postRepository.delete(post);
    }

    @Override
    public void likePost(Long id, String token) {
        User user = getUser(token);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException("Post topilmadi"));

        boolean alreadyLiked = likeRepository.existsByPostIdAndUserId(post.getId(), user.getId());
        if (alreadyLiked) {
            throw new PostException("Siz bu postni allaqachon yoqtirgansiz");
        }

        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
        Like like = new Like();
        like.setPostId(post.getId());
        like.setUserId(user.getId());
        likeRepository.save(like);
    }

    @Override
    public void sharePost(Long id, String token) {
        User user = getUser(token);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException("Post topilmadi"));

        post.setShareCount(post.getShareCount() + 1);
        postRepository.save(post);
    }

    @Override
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException("Post topilmadi"));
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        return PostResponseDto.builder()
                .id(post.getId())
                .key(post.getKey())
                .text(post.getText())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .shareCount(post.getShareCount())
                .isMedia(post.isMedia())
                .isBlocked(post.isBlocked())
                .build();
    }

    @Override
    public List<PostResponseDto> getAllUserPosts(String token) {
        User user = getUser(token);
        List<Post> posts = postRepository.getAllByUserId((user.getId()));
        if (posts.isEmpty()) {
            throw new PostException("Post topilmadi");
        }
        return posts.stream()
                .map(post -> PostResponseDto.builder()
                        .id(post.getId())
                        .key(post.getKey())
                        .text(post.getText())
                        .likeCount(post.getLikeCount())
                        .viewCount(post.getViewCount())
                        .shareCount(post.getShareCount())
                        .isMedia(post.isMedia())
                        .isBlocked(post.isBlocked())
                        .build())
                .toList();
    }

    @Override
    public PostResponseDto editPost(Long id, String text, MultipartFile media, String token) {
        User user = getUser(token);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException("Post topilmadi"));

        if (!post.getUserId().equals(user.getId())) {
            throw new PostException("Siz bu postni o'zgartira olmaysiz");
        }

        post.setText(text);

        if (media != null && !media.isEmpty()) {
            String key = UUID.randomUUID().toString();

            try {
                String contentType = media.getContentType();
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(media.getSize());
                metadata.setContentType(contentType);

                if (!amazonS3.doesBucketExistV2(bucketName)) {
                    amazonS3.createBucket(bucketName);
                }

                amazonS3.putObject(new PutObjectRequest(
                        bucketName,
                        key,
                        media.getInputStream(),
                        metadata
                ));

                post.setKey(key);
                post.setMedia(true);
            } catch (IOException e) {
                throw new RuntimeException("Xatolik yuz berdi: " + e.getMessage());
            }
        }

        postRepository.save(post);

        return PostResponseDto.builder()
                .id(post.getId())
                .key(post.getKey())
                .text(post.getText())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .shareCount(post.getShareCount())
                .isMedia(post.isMedia())
                .isBlocked(post.isBlocked())
                .build();
    }


}