package com.nith.flex.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nith.flex.portfolio.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Find all comments for a specific blog post, ordered by creation date
    List<Comment> findByBlogPostIdOrderByCreatedAtDesc(Long blogPostId);

    // Find a specific comment by its ID and user ID (for ownership check)
    Optional<Comment> findByIdAndUserId(Long commentId, Long userId);
}
