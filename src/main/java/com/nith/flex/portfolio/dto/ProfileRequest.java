package com.nith.flex.portfolio.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
	@Size(max = 500, message = "Bio must be max 500 characters")
	private String bio; // Bio can be optional (not @NotBlank)

	@Size(max = 100, message = "Headline must be max 100 characters")
	private String headline; // Headline can be optional

	@URL(message = "Profile picture URL must be a valid URL")
	@Size(max = 255, message = "Profile picture URL must be max 255 characters")
	private String profilePictureUrl; // Can be optional, or provided by an upload service

	@URL(message = "LinkedIn URL must be a valid URL")
	@Size(max = 255, message = "LinkedIn URL must be max 255 characters")
	private String linkedinUrl;

	@URL(message = "GitHub URL must be a valid URL")
	@Size(max = 255, message = "GitHub URL must be max 255 characters")
	private String githubUrl;

	@Size(max = 100, message = "Location must be max 100 characters")
	private String location;

	// Optional: Add phone number validation pattern if strict format is required
	@Pattern(regexp = "^[+]?[0-9]{8,15}$", message = "Phone number must be valid (e.g., +85512345678)")
	private String phoneNumber;
}
