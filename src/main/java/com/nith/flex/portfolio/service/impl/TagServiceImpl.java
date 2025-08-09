package com.nith.flex.portfolio.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nith.flex.portfolio.dto.TagRequest;
import com.nith.flex.portfolio.dto.TagResponse;
import com.nith.flex.portfolio.entity.BlogPost;
import com.nith.flex.portfolio.entity.Tag;
import com.nith.flex.portfolio.exception.ApiException;
import com.nith.flex.portfolio.exception.ResourceException;
import com.nith.flex.portfolio.repository.BlogPostRepository;
import com.nith.flex.portfolio.repository.TagRepository;
import com.nith.flex.portfolio.service.TagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final BlogPostRepository blogPostRepository;

    private TagResponse convertToDto(Tag tag) {
        if (tag == null) return null;
        return new TagResponse(tag.getId(), tag.getName());
    }

    @Override
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponse createTag(TagRequest request) {
        // Check for duplicate tag name
        tagRepository.findByNameIgnoreCase(request.getName())
                .ifPresent(t -> {
                    throw new ApiException(HttpStatus.CONFLICT, "Tag with this name already exists.", "NAME_CONFLICT");
                });

        Tag tag = new Tag();
        tag.setName(request.getName());
        Tag savedTag = tagRepository.save(tag);
        return convertToDto(savedTag);
    }

    @Override
    public void deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceException("Tag", tagId, "NOT_FOUND"));
        tagRepository.delete(tag);
    }

    @Override
    public void addTagToPost(Long userId, Long postId, Long tagId) {
        BlogPost blogPost = blogPostRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new ResourceException("Blog", postId, "NOT_FOUND"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceException("Tag", tagId, "NOT_FOUND"));

        // Use the helper method on the BlogPost entity to manage the relationship
        blogPost.addTag(tag);
        blogPostRepository.save(blogPost);
    }

    @Override
    public void removeTagFromPost(Long userId, Long postId, Long tagId) {
        BlogPost blogPost = blogPostRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new ResourceException("Blog", postId, "NOT_FOUND"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceException("Tag", tagId, "NOT_FOUND"));

        blogPost.removeTag(tag);
        blogPostRepository.save(blogPost);
    }
}
