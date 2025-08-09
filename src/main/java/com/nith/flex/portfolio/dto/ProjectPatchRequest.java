package com.nith.flex.portfolio.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPatchRequest {

    @Size(max = 100, message = "Project title must be max 100 characters")
    private String title;

    @Size(max = 2000, message = "Project description must be max 2000 characters")
    private String description;

    @URL(message = "Project URL must be a valid URL")
    @Size(max = 255, message = "Project URL must be max 255 characters")
    private String projectUrl;

    @URL(message = "GitHub URL must be a valid URL")
    @Size(max = 255, message = "GitHub URL must be max 255 characters")
    private String githubUrl;

    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isFeatured; // Nullable for partial update
}
