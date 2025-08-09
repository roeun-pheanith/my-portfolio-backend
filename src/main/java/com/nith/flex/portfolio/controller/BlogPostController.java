package com.nith.flex.portfolio.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nith.flex.portfolio.config.security.AuthUser;
import com.nith.flex.portfolio.dto.BlogPostRequest;
import com.nith.flex.portfolio.dto.BlogPostResponse;
import com.nith.flex.portfolio.service.BlogPostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    // --- Public Endpoints ---

    // GET /api/posts
    // Get all PUBLISHED blog posts
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<BlogPostResponse>> getPublishedPosts() {
        List<BlogPostResponse> posts = blogPostService.getPublishedPosts();
        return ResponseEntity.ok(posts);
    }

    // GET /api/posts/slug/{slug}
    // Get a single PUBLISHED blog post by its slug
    @GetMapping("/slug/{slug}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<BlogPostResponse> getPublishedPostBySlug(@PathVariable String slug) {
        BlogPostResponse post = blogPostService.getPublishedPostBySlug(slug);
        return ResponseEntity.ok(post);
    }

    // --- Authenticated User Endpoints ---

    // GET /api/posts/{postId}
    // Get a specific post by ID (can be draft or published, requires authentication)
    @GetMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BlogPostResponse> getPostById(@PathVariable Long postId) {
        BlogPostResponse post = blogPostService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    // GET /api/posts/my-posts
    // Get all posts for the authenticated user (includes drafts)
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BlogPostResponse>> getMyPosts(Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        List<BlogPostResponse> posts = blogPostService.getPostsByUserId(userPrincipal.getId());
        return ResponseEntity.ok(posts);
    }

    // POST /api/posts
    // Create a new post for the authenticated user
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BlogPostResponse> createPost(
            @Valid @RequestBody BlogPostRequest request,
            Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        BlogPostResponse createdPost = blogPostService.createPost(userPrincipal.getId(), request);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // PUT /api/posts/{postId}
    // Update an existing post (must be owned by the user)
    @PutMapping("/{postId}")
    @PreAuthorize("@blogPostSecurity.isOwnerOrAdmin(#postId)")
    public ResponseEntity<BlogPostResponse> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody BlogPostRequest request,
            Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        BlogPostResponse updatedPost = blogPostService.updatePost(userPrincipal.getId(), postId, request);
        return ResponseEntity.ok(updatedPost);
    }

    // DELETE /api/posts/{postId}
    // Delete an existing post (must be owned by the user)
    @DeleteMapping("/{postId}")
    @PreAuthorize("@blogPostSecurity.isOwnerOrAdmin(#postId)")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        blogPostService.deletePost(userPrincipal.getId(), postId);
        return ResponseEntity.noContent().build();
    }
}
