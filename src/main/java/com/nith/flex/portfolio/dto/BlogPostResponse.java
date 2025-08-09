package com.nith.flex.portfolio.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostResponse {
    private Long id;
    private Long userId;
    private String username;

    private Set<TagResponse> tags;
    private String title;
    private String slug;
    private String content;
    private String excerpt;
    private String thumbnailUrl;
    private Boolean isPublished;
    private LocalDateTime publishedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
