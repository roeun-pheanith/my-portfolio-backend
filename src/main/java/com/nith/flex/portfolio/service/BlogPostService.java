package com.nith.flex.portfolio.service;

import java.util.List;

import com.nith.flex.portfolio.dto.BlogPostRequest;
import com.nith.flex.portfolio.dto.BlogPostResponse;

public interface BlogPostService {
    // Publicly accessible methods
    List<BlogPostResponse> getPublishedPosts();
    BlogPostResponse getPublishedPostBySlug(String slug);

    // Authenticated user methods
    List<BlogPostResponse> getPostsByUserId(Long userId); // Includes drafts
    BlogPostResponse getPostById(Long postId); // For authenticated access, may be a draft
    BlogPostResponse createPost(Long userId, BlogPostRequest request);
    BlogPostResponse updatePost(Long userId, Long postId, BlogPostRequest request);
    void deletePost(Long userId, Long postId);
}
