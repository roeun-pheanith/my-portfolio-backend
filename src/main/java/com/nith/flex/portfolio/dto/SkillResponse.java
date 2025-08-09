package com.nith.flex.portfolio.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillResponse {
    private Long id;
    private Long userId; // ID of the user who owns this skill
    private String username; // Username of the skill owner

    private String name;
    private String proficiency;
    private boolean isFeatured;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
