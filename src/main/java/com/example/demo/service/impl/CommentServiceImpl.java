package com.example.demo.service.impl;

import com.example.demo.domain.dto.request_dto.CommentRequestDto;
import com.example.demo.domain.dto.response_dto.CommentResponseDto;
import com.example.demo.domain.dto.response_dto.CommentUserResponseDto;
import com.example.demo.domain.entity.Comment;
import com.example.demo.domain.entity.Post;
import com.example.demo.domain.entity.User;
import com.example.demo.exceptions.PostException;
import com.example.demo.exceptions.UserException;
import com.example.demo.jwt_utils.JwtTokenProvider;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostService postService;
    private final CommentRepository commentRepository;

    private CommentUserResponseDto findCommentedUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
        return new CommentUserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getUsername()
        );
    }

    @Override
    public String createComment(CommentRequestDto commentRequestDto, String token) {
        User user = postService.getUser(token);

        if(postRepository.findById(commentRequestDto.getPostId()).isEmpty()) {
            throw new UserException("Bunday post mavjud emas");
        }

        Comment comment = new Comment().builder()
                .text(commentRequestDto.getText())
                .userId(user.getId())
                .postId(commentRequestDto.getPostId())
                .build();
        commentRepository.save(comment);
        return "Comment created successfully";
    }

    @Override
    public List<CommentResponseDto> getAllCommentsByPostId(Long postId) {
        if(postRepository.findById(postId).isEmpty()) {
            throw new UserException("Bunday post mavjud emas");
        }
        List<Comment> comments = commentRepository.getAllByPostId(postId);
        return comments.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getText(),
                        findCommentedUser(comment.getUserId()),
                        comment.getLikeCount()
                )).toList();
    }


    @Override
    public void deleteComment(Long commentId, String token) {
        User user = postService.getUser(token);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new PostException("Comment topilmadi"));
        if (!comment.getUserId().equals(user.getId())) {
            Post post = postRepository.findById(comment.getPostId())
                    .orElseThrow(() -> new PostException("Post topilmadi"));
            if (post.getUserId().equals(user.getId())){
                commentRepository.delete(comment);
            }else{
                throw new UserException("Siz bu kommentariyani o'chira olmaysiz");
            }
        }
        commentRepository.delete(comment);
    }
}
