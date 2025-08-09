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
public class SkillRequest {
    @NotBlank(message = "Skill name cannot be empty")
    @Size(max = 50, message = "Skill name must be max 50 characters")
    private String name;

    @NotBlank(message = "Proficiency level cannot be empty")
    @Size(max = 20, message = "Proficiency must be max 20 characters")
    // Consider using @Pattern or custom validation if you use a fixed set of proficiency strings.
    private String proficiency; // e.g., "Beginner", "Intermediate", "Advanced", "Expert"

    @NotNull(message = "isFeatured cannot be null")
    private Boolean isFeatured = false;
}