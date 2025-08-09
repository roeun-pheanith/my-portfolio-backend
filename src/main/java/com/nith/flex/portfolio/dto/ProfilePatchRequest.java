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
public class ProfilePatchRequest {

	@Size(max = 500, message = "Bio must be max 500 characters")
	private String bio;

	@Size(max = 100, message = "Headline must be max 100 characters")
	private String headline;

	@URL(message = "Profile picture URL must be a valid URL")
	@Size(max = 255, message = "Profile picture URL must be max 255 characters")
	private String profilePictureUrl;

	@URL(message = "LinkedIn URL must be a valid URL")
	@Size(max = 255, message = "LinkedIn URL must be max 255 characters")
	private String linkedinUrl;

	@URL(message = "GitHub URL must be a valid URL")
	@Size(max = 255, message = "GitHub URL must be max 255 characters")
	private String githubUrl;

	@URL(message = "Personal website URL must be a valid URL")
	@Size(max = 255, message = "Personal website URL must be max 255 characters")
	private String personalWebsiteUrl;

	@Size(max = 100, message = "Location must be max 100 characters")
	private String location;

	@Pattern(regexp = "^[+]?[0-9]{8,15}$", message = "Phone number must be valid (e.g., +85512345678)")
	private String phoneNumber;
}
