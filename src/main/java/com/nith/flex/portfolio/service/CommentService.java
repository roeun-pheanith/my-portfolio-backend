package com.nith.flex.portfolio.service;

import java.util.List;

import com.nith.flex.portfolio.dto.CommentRequest;
import com.nith.flex.portfolio.dto.CommentResponse;

public interface CommentService {
    List<CommentResponse> getCommentsByPostId(Long postId);
    CommentResponse createComment(Long postId, CommentRequest request, Long userId);
    void deleteComment(Long commentId, Long userId);
}
