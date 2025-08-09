package com.nith.flex.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nith.flex.portfolio.entity.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByUserIdOrderByPublishedAtDesc(Long userId);
    Optional<BlogPost> findByIdAndUserId(Long postId, Long userId);
    List<BlogPost> findByIsPublishedTrueOrderByPublishedAtDesc();
    Optional<BlogPost> findBySlugAndIsPublishedTrue(String slug);
    Optional<BlogPost> findBySlug(String slug);
}
