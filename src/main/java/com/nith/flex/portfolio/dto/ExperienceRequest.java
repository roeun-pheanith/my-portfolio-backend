package com.nith.flex.portfolio.dto;

import java.time.LocalDate;

import com.nith.flex.portfolio.helper.ExperienceType;

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
public class ExperienceRequest {
	@NotBlank(message = "Company cannot be empty")
	@Size(max = 100, message = "Company name must be max 100 characters")
	private String company;

	@NotBlank(message = "Job title cannot be empty")
	@Size(max = 100, message = "Job title must be max 100 characters")
	private String title;

	@Size(max = 100, message = "Location must be max 100 characters")
	private String location;

	@NotNull(message = "Start date cannot be empty")
	@PastOrPresent(message = "Start date cannot be in the future")
	private LocalDate startDate;

	private LocalDate endDate;

	@NotNull(message = "isCurrent cannot be null")
	private Boolean isCurrent = false;

	@NotBlank(message = "Description cannot be empty")
	@Size(max = 2000, message = "Description must be max 2000 characters")
	private String description;

	@NotNull(message = "Experience type cannot be null")
	private ExperienceType type;
}
