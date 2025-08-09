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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nith.flex.portfolio.config.security.AuthUser;
import com.nith.flex.portfolio.dto.TagRequest;
import com.nith.flex.portfolio.dto.TagResponse;
import com.nith.flex.portfolio.service.TagService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    // --- Public Endpoints ---
    // GET /api/tags
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    // --- Admin-only Endpoints ---
    // POST /api/tags
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagRequest request) {
        TagResponse createdTag = tagService.createTag(request);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    // DELETE /api/tags/{tagId}
    @DeleteMapping("/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }

    // --- Tag-Post Relationship Endpoints (Authenticated/Owner-only) ---
    // POST /api/posts/{postId}/tags/{tagId}
    @PostMapping("/posts/{postId}/tags/{tagId}")
    @PreAuthorize("@blogPostSecurity.isOwnerOrAdmin(#postId)")
    public ResponseEntity<Void> addTagToPost(
            @PathVariable Long postId,
            @PathVariable Long tagId,
            Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        tagService.addTagToPost(userPrincipal.getId(), postId, tagId);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/posts/{postId}/tags/{tagId}
    @DeleteMapping("/posts/{postId}/tags/{tagId}")
    @PreAuthorize("@blogPostSecurity.isOwnerOrAdmin(#postId)")
    public ResponseEntity<Void> removeTagFromPost(
            @PathVariable Long postId,
            @PathVariable Long tagId,
            Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        tagService.removeTagFromPost(userPrincipal.getId(), postId, tagId);
        return ResponseEntity.noContent().build();
    }
}
