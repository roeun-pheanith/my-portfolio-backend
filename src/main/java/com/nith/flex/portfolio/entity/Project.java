package com.nith.flex.portfolio.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Project extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;
	private String projectUrl;
	private String githubUrl;
	private String technologies;
	private Boolean isFeature = false;
	@ManyToOne
	private User user;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProjectImage> images = new HashSet<>();

	public void addImage(ProjectImage image) {
		images.add(image);
		image.setProject(this);
	}

	public void removeImage(ProjectImage image) {
		images.remove(image);
		image.setProject(null);
	}
}
