package com.nith.flex.portfolio.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nith.flex.portfolio.dto.ProjectPatchRequest;
import com.nith.flex.portfolio.dto.ProjectRequest;
import com.nith.flex.portfolio.dto.ProjectResponse;
import com.nith.flex.portfolio.entity.Project;
import com.nith.flex.portfolio.entity.User;
import com.nith.flex.portfolio.exception.ApiException;
import com.nith.flex.portfolio.exception.ResourceException;
import com.nith.flex.portfolio.repository.ProjectRepository;
import com.nith.flex.portfolio.repository.UserRepository;
import com.nith.flex.portfolio.service.ProjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	// Helper to convert Entity to Response DTO
	private ProjectResponse convertToDto(Project project) {
		if (project == null)
			return null;
		ProjectResponse dto = new ProjectResponse();
		dto.setId(project.getId());
		dto.setUserId(project.getUser().getId());
		dto.setUsername(project.getUser().getUsername());
		dto.setTitle(project.getTitle());
		dto.setDescription(project.getDescription());
		dto.setProjectUrl(project.getProjectUrl());
		dto.setGithubUrl(project.getGithubUrl());
		dto.setStartDate(project.getStartDate());
		dto.setEndDate(project.getEndDate());
		dto.setFeatured(project.getIsFeature()); // Use isFeatured directly
		dto.setCreatedAt(project.getCreateAt());
		dto.setUpdatedAt(project.getUpdateAt());
		return dto;
	}

	@Override
	public Page<ProjectResponse> getAllPublicProjects(Pageable pageable) {
		return projectRepository.findByIsFeatureTrue(pageable).map(this::convertToDto);
	}

	@Override
	public ProjectResponse getProjectById(Long projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceException("Project", projectId, "PROJECT_NOT_FOUND"));
		return convertToDto(project);
	}

	@Override
	public Page<ProjectResponse> getMyProjects(Long userId, Pageable pageable) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceException("User", userId, "PROJECT_NOT_FOUND"));
		return projectRepository.findByUserId(userId, pageable).map(this::convertToDto);
	}

	@Override
	public ProjectResponse createProject(Long userId, ProjectRequest request) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceException("User", userId, "USER_NOT_FOUND"));
		Project project = new Project();
		project.setUser(user);
		project.setTitle(request.getTitle());
		project.setDescription(request.getDescription());
		project.setProjectUrl(request.getProjectUrl());
		project.setGithubUrl(request.getGithubUrl());
		project.setStartDate(request.getStartDate());
		project.setEndDate(request.getEndDate());
		project.setIsFeature(request.getIsFeatured()); // Use getIsFeatured for Boolean
		if (project.getStartDate() != null && project.getEndDate() != null
				&& project.getEndDate().isBefore(project.getStartDate())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "End date cannot be before start date.",
					"INVALID_TIME_STEMP");
		}
		Project savedProject = projectRepository.save(project);
		return convertToDto(savedProject);
	}

	@Override
	public ProjectResponse updateProject(Long userId, Long projectId, ProjectRequest request) {
		Project project = projectRepository.findByIdAndUserId(projectId, userId)
				.orElseThrow(() -> new ResourceException("Project", projectId, "PROJECT_NOT_FOUND"));
		project.setTitle(request.getTitle());
		project.setDescription(request.getDescription());
		project.setProjectUrl(request.getProjectUrl());
		project.setGithubUrl(request.getGithubUrl());
		project.setStartDate(request.getStartDate());
		project.setEndDate(request.getEndDate());
		project.setIsFeature(request.getIsFeatured());

		// Optional: Custom validation for endDate > startDate
		if (project.getStartDate() != null && project.getEndDate() != null
				&& project.getEndDate().isBefore(project.getStartDate())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "End date cannot be before start date.",
					"INVALID_TIME_STEMP");
		}

		Project updatedProject = projectRepository.save(project);
		return convertToDto(updatedProject);
	}

	@Override
	public ProjectResponse patchProject(Long userId, Long projectId, ProjectPatchRequest request) {
		Project project = projectRepository.findByIdAndUserId(projectId, userId)
				.orElseThrow(() -> new ResourceException("Project", projectId, "PROJECT_NOT_FOUND"));
		if (request.getTitle() != null)
			project.setTitle(request.getTitle());
		if (request.getDescription() != null)
			project.setDescription(request.getDescription());
		if (request.getProjectUrl() != null)
			project.setProjectUrl(request.getProjectUrl());
		if (request.getGithubUrl() != null)
			project.setGithubUrl(request.getGithubUrl());
		if (request.getStartDate() != null)
			project.setStartDate(request.getStartDate());
		if (request.getEndDate() != null)
			project.setEndDate(request.getEndDate());
		if (request.getIsFeatured() != null)
			project.setIsFeature(request.getIsFeatured());

		// Optional: Custom validation for endDate > startDate
		if (project.getStartDate() != null && project.getEndDate() != null
				&& project.getEndDate().isBefore(project.getStartDate())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "End date cannot be before start date.",
					"INVALID_TIME_STEMP");
		}

		Project patchedProject = projectRepository.save(project);
		return convertToDto(patchedProject);
	}

	@Override
	public void deleteProject(Long userId, Long projectId) {
		Project project = projectRepository.findByIdAndUserId(projectId, userId)
				.orElseThrow(() -> new ResourceException("Project", projectId, "PROJECT_NOT_FOUND"));

		projectRepository.delete(project);

	}

}
