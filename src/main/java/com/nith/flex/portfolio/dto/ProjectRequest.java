package com.nith.flex.portfolio.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @NotBlank(message = "Project title cannot be empty")
    @Size(max = 100, message = "Project title must be max 100 characters")
    private String title;

    @NotBlank(message = "Project description cannot be empty")
    @Size(max = 2000, message = "Project description must be max 2000 characters")
    private String description;

    @URL(message = "Project URL must be a valid URL")
    @Size(max = 255, message = "Project URL must be max 255 characters")
    private String projectUrl; // Optional

    @URL(message = "GitHub URL must be a valid URL")
    @Size(max = 255, message = "GitHub URL must be max 255 characters")
    private String githubUrl; // Optional

    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate; // Optional

    private LocalDate endDate; // Optional (e.g., if project is ongoing)

    // No validation for endDate > startDate here, do it in service or custom annotation if needed

    @NotNull(message = "isFeatured cannot be null") // Or default to false in service
    private Boolean isFeatured = false; // Default value, can be overridden by request
}
