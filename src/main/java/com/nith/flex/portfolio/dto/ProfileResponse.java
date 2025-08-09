package com.nith.flex.portfolio.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
	private Long id; // The Profile's own ID
	private Long userId; // The ID of the User this profile belongs to
	private String username; // Username of the associated user (for convenience in public view)
	private String email; // Email of the associated user (if desired for public view, otherwise omit)

	private String bio;
	private String headline;
	private String profilePictureUrl;
	private String linkedinUrl;
	private String githubUrl;
	private String personalWebsiteUrl;
	private String location;
	private String phoneNumber;
	private LocalDateTime createAt;
	private LocalDateTime updateAt;

}
