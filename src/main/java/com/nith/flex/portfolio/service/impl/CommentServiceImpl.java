package com.nith.flex.portfolio.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nith.flex.portfolio.dto.CommentRequest;
import com.nith.flex.portfolio.dto.CommentResponse;
import com.nith.flex.portfolio.entity.BlogPost;
import com.nith.flex.portfolio.entity.Comment;
import com.nith.flex.portfolio.entity.User;
import com.nith.flex.portfolio.exception.ResourceException;
import com.nith.flex.portfolio.repository.BlogPostRepository;
import com.nith.flex.portfolio.repository.CommentRepository;
import com.nith.flex.portfolio.repository.UserRepository;
import com.nith.flex.portfolio.service.CommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    private CommentResponse convertToDto(Comment comment) {
        if (comment == null) return null;
        CommentResponse dto = new CommentResponse();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setPostId(comment.getBlogPost().getId());
        if (comment.getUser() != null) {
            dto.setUserId(comment.getUser().getId());
            dto.setUsername(comment.getUser().getUsername());
        }
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

    @Override
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceException("Blog", postId, "NOT_FOUND"));
        return commentRepository.findByBlogPostIdOrderByCreatedAtDesc(postId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse createComment(Long postId, CommentRequest request, Long userId) {
        BlogPost blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceException("Blog", postId, "NOT_FOUND"));

        Comment comment = new Comment();
        comment.setBlogPost(blogPost);
        comment.setContent(request.getContent());

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceException("User", userId, "NOT_FOUND"));
            comment.setUser(user);
        }

        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new ResourceException("User", userId, "NOT_FOUND"));

        commentRepository.delete(comment);
    }
}
