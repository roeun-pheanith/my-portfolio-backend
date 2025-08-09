package com.nith.flex.portfolio.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillPatchRequest {
    @Size(max = 50, message = "Skill name must be max 50 characters")
    private String name;

    @Size(max = 20, message = "Proficiency must be max 20 characters")
    private String proficiency;

    private Boolean isFeatured;
}