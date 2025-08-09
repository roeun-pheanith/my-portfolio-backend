package com.nith.flex.portfolio.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private Long postId;
    private Long userId; // Null for anonymous comments
    private String username; // Null for anonymous comments
    private LocalDateTime createdAt;
}
