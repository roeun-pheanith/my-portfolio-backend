package com.nith.flex.portfolio.service;

import java.util.List;

import com.nith.flex.portfolio.dto.TagRequest;
import com.nith.flex.portfolio.dto.TagResponse;

public interface TagService {
    List<TagResponse> getAllTags();
    TagResponse createTag(TagRequest request);
    void deleteTag(Long tagId);
    void addTagToPost(Long userId, Long postId, Long tagId);
    void removeTagFromPost(Long userId, Long postId, Long tagId);
}
