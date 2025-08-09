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
import com.nith.flex.portfolio.dto.CommentRequest;
import com.nith.flex.portfolio.dto.CommentResponse;
import com.nith.flex.portfolio.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	// --- Public Endpoints ---
	// GET /api/posts/{postId}/comments
	@GetMapping
	@PreAuthorize("permitAll()")
	public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId) {
		List<CommentResponse> comments = commentService.getCommentsByPostId(postId);
		return ResponseEntity.ok(comments);
	}

	// --- Authenticated User Endpoints ---
	// POST /api/posts/{postId}/comments
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId,
			@Valid @RequestBody CommentRequest request, Authentication authentication) {
		AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
		CommentResponse createdComment = commentService.createComment(postId, request, userPrincipal.getId());
		return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
	}

	// DELETE /api/posts/{postId}/comments/{commentId}
	@DeleteMapping("/{commentId}")
	@PreAuthorize("@commentSecurity.isOwnerOrAdmin(#commentId)")
	public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
			Authentication authentication) {
		AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
		commentService.deleteComment(commentId, userPrincipal.getId());
		return ResponseEntity.noContent().build();
	}
}
