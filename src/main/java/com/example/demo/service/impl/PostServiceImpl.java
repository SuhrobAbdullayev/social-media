package com.example.demo.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.demo.domain.entity.Post;
import com.example.demo.domain.entity.User;
import com.example.demo.exceptions.UserException;
import com.example.demo.jwt_utils.JwtTokenProvider;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AmazonS3 amazonS3;
    private final MinioClient minioClient;

    @Value("${services.s3.bucket-name}")
    private String bucketName;

    @Override
    public String createPost(MultipartFile media, String text, String token) {
        String rawToken = token.replace("Bearer ", "");
        String username = jwtTokenProvider.getUser(rawToken).toLowerCase();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UserException("Bunday foydalanuvchi mavjud emas");
        }

        String key = null;

        // Handle file upload if media is provided
        if (media != null && !media.isEmpty()) {
            key = UUID.randomUUID().toString(); // Unique file key

            try {
                // Manually set the content type if it's null or incorrect
                String contentType = media.getContentType();
                if (contentType == null) {
                    contentType = "application/octet-stream";  // Default fallback type
                }

                // Set metadata for S3
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(media.getSize());
                metadata.setContentType(contentType);

                // Ensure the bucket exists or create it
                if (!amazonS3.doesBucketExistV2(bucketName)) {
                    amazonS3.createBucket(bucketName);
                }

                // Upload the media to S3
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

        // Build and save the post
        Post.PostBuilder builder = Post.builder()
                .text(text)
                .userId(user.getId());

        if (key != null) {
            builder.key(key).isMedia(true);
        }

        Post post = builder.build();
        postRepository.save(post);

        // Return the media URL or just a text post message
        return (key != null)
                ? amazonS3.getUrl(bucketName, key).toString()
                : "Tekstli Post yaratildi";
    }


    @Override
    public InputStream downloadFile(String key) throws Exception {
        try {
            S3Object s3Object = amazonS3.getObject(bucketName, key);
            return s3Object.getObjectContent();
        } catch (Exception e) {
            throw new RuntimeException("Error while downloading file from S3");
        }
    }


}