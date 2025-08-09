package com.nith.flex.portfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Profile extends AuditEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String fullName;
	private String headline;
	private String bio;
	private String email;
	private String phoneNumber;
	private String location;
	private String linkedinUrl;
	private String websiteUrl;
	private String profilePictureUrl;
	private String githubUrl;
	private String resumeUrl;
	
	@OneToOne(fetch = FetchType.LAZY)
	private User user;
}
