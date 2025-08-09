package com.nith.flex.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostRequest {
    @NotBlank(message = "Title cannot be empty")
    @Size(max = 200, message = "Title must be max 200 characters")
    private String title;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    @Size(max = 2000, message = "Excerpt must be max 2000 characters")
    private String excerpt;

    private String thumbnailUrl;

    @NotNull(message = "isPublished cannot be null")
    private Boolean isPublished;
}
