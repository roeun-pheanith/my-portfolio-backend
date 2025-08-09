package com.nith.flex.portfolio.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nith.flex.portfolio.dto.ProjectPatchRequest;
import com.nith.flex.portfolio.dto.ProjectRequest;
import com.nith.flex.portfolio.dto.ProjectResponse;

public interface ProjectService {
	// Publicly accessible methods
    Page<ProjectResponse> getAllPublicProjects(Pageable pageable);
    ProjectResponse getProjectById(Long projectId);

    // User-specific (authenticated) methods
    Page<ProjectResponse> getMyProjects(Long userId, Pageable pageable);
    ProjectResponse createProject(Long userId, ProjectRequest request);
    ProjectResponse updateProject(Long userId, Long projectId, ProjectRequest request);
    ProjectResponse patchProject(Long userId, Long projectId, ProjectPatchRequest request);
    void deleteProject(Long userId, Long projectId);
}
