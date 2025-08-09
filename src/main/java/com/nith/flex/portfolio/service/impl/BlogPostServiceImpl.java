package com.nith.flex.portfolio.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nith.flex.portfolio.dto.BlogPostRequest;
import com.nith.flex.portfolio.dto.BlogPostResponse;
import com.nith.flex.portfolio.dto.TagRequest;
import com.nith.flex.portfolio.dto.TagResponse;
import com.nith.flex.portfolio.entity.BlogPost;
import com.nith.flex.portfolio.entity.User;
import com.nith.flex.portfolio.exception.ApiException;
import com.nith.flex.portfolio.exception.ResourceException;
import com.nith.flex.portfolio.repository.BlogPostRepository;
import com.nith.flex.portfolio.repository.UserRepository;
import com.nith.flex.portfolio.service.BlogPostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {

	private final BlogPostRepository blogPostRepository;
	private final UserRepository userRepository;

	private BlogPostResponse convertToDto(BlogPost post) {
		if (post == null)
			return null;
		BlogPostResponse dto = new BlogPostResponse();
		dto.setId(post.getId());
		dto.setUserId(post.getUser().getId());
		dto.setUsername(post.getUser().getUsername());
		dto.setTitle(post.getTitle());
		dto.setSlug(post.getSlug());
		dto.setContent(post.getContent());
		dto.setExcerpt(post.getExcerpt());
		dto.setTags(post.getTags().stream().map(tag -> new TagResponse(tag.getId(), tag.getName()))
				.collect(Collectors.toSet()));
		dto.setThumbnailUrl(post.getThumbnailUrl());
		dto.setIsPublished(post.getIsPublished());
		dto.setPublishedAt(post.getPublishedAt());
		dto.setCreatedAt(post.getCreateAt());
		dto.setUpdatedAt(post.getUpdateAt());
		return dto;
	}

	// --- Public Access Methods ---
	@Override
	public List<BlogPostResponse> getPublishedPosts() {
		return blogPostRepository.findByIsPublishedTrueOrderByPublishedAtDesc().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public BlogPostResponse getPublishedPostBySlug(String slug) {
		BlogPost post = blogPostRepository.findBySlugAndIsPublishedTrue(slug)
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
						"Blog post with slug = %s not found".formatted(slug), "POST_NOT_FOUND"));
		return convertToDto(post);
	}

	// --- Authenticated User Methods ---
	@Override
	public List<BlogPostResponse> getPostsByUserId(Long userId) {
		userRepository.findById(userId).orElseThrow(() -> new ResourceException("User", userId, "USER_NOT_FOUND"));
		return blogPostRepository.findByUserIdOrderByPublishedAtDesc(userId).stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public BlogPostResponse getPostById(Long postId) {
		BlogPost post = blogPostRepository.findById(postId)
				.orElseThrow(() -> new ResourceException("Post", postId, "POST_NOT_FOUND"));
		return convertToDto(post);
	}

	@Override
	public BlogPostResponse createPost(Long userId, BlogPostRequest request) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceException("User", userId, "USER_NOT_FOUND"));

		String slug = generateSlug(request.getTitle());
		if (blogPostRepository.findBySlug(slug).isPresent()) {
			throw new ApiException(HttpStatus.CONFLICT, "A blog post with this title already exists.", "SLUG_CONFLICT");
		}

		BlogPost post = new BlogPost();
		post.setUser(user);
		post.setTitle(request.getTitle());
		post.setSlug(slug);
		post.setContent(request.getContent());
		post.setExcerpt(request.getExcerpt());
		post.setThumbnailUrl(request.getThumbnailUrl());
		post.setIsPublished(request.getIsPublished());

		if (request.getIsPublished()) {
			post.setPublishedAt(LocalDateTime.now());
		}

		BlogPost savedPost = blogPostRepository.save(post);
		return convertToDto(savedPost);
	}

	@Override
	public BlogPostResponse updatePost(Long userId, Long postId, BlogPostRequest request) {
		BlogPost existingPost = blogPostRepository.findByIdAndUserId(postId, userId)
				.orElseThrow(() -> new ResourceException("Post", postId, "POST_NOT_FOUND"));

		if (!existingPost.getTitle().equals(request.getTitle())) {
			String newSlug = generateSlug(request.getTitle());
			Optional<BlogPost> postWithSameSlug = blogPostRepository.findBySlug(newSlug);
			if (postWithSameSlug.isPresent() && !postWithSameSlug.get().getId().equals(postId)) {
				throw new ApiException(HttpStatus.CONFLICT, "A blog post with this title already exists.",
						"SLUG_CONFLICT");
			}
			existingPost.setSlug(newSlug);
		}

		// Set publishedAt only if the post is transitioning from unpublished to
		// published
		if (!existingPost.getIsPublished() && request.getIsPublished()) {
			existingPost.setPublishedAt(LocalDateTime.now());
		}

		existingPost.setTitle(request.getTitle());
		existingPost.setContent(request.getContent());
		existingPost.setExcerpt(request.getExcerpt());
		existingPost.setThumbnailUrl(request.getThumbnailUrl());
		existingPost.setIsPublished(request.getIsPublished());

		BlogPost updatedPost = blogPostRepository.save(existingPost);
		return convertToDto(updatedPost);
	}

	@Override
	public void deletePost(Long userId, Long postId) {
		BlogPost post = blogPostRepository.findByIdAndUserId(postId, userId)
				.orElseThrow(() -> new ResourceException("Post", postId, "BLOG_NOT_FOUND"));
		blogPostRepository.delete(post);
	}

	private String generateSlug(String title) {
		String slug = StringUtils.trimAllWhitespace(title.toLowerCase());
		slug = slug.replaceAll("[^a-z0-9\\s-]", "");
		slug = slug.replaceAll("\\s+", "-");
		return slug;
	}

}
