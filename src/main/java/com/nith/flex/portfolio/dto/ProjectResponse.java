package com.nith.flex.portfolio.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private Long userId; // ID of the user who owns this project
    private String username; // Username of the project owner

    private String title;
    private String description;
    private String projectUrl;
    private String githubUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isFeatured; // Using primitive boolean as it's always returned

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
