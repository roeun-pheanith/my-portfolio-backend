package com.nith.flex.portfolio.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nith.flex.portfolio.config.security.AuthUser;
import com.nith.flex.portfolio.dto.ProjectPatchRequest;
import com.nith.flex.portfolio.dto.ProjectRequest;
import com.nith.flex.portfolio.dto.ProjectResponse;
import com.nith.flex.portfolio.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
	private final ProjectService projectService;
	
	@GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<ProjectResponse>> getAllPublicProjects(Pageable pageable) {
        Page<ProjectResponse> projects = projectService.getAllPublicProjects(pageable);
        return ResponseEntity.ok(projects);
    }
	
	@GetMapping("/{projectId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long projectId) {
        ProjectResponse project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }
	
	@GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ProjectResponse>> getMyProjects(Authentication authentication, Pageable pageable) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        Page<ProjectResponse> projects = projectService.getMyProjects(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(projects);
    }
	
	@PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        ProjectResponse createdProject = projectService.createProject(userPrincipal.getId(), request);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }
	
	@PutMapping("/{projectId}")
    @PreAuthorize("@projectSecurity.isOwnerOrAdmin(#projectId)") // Custom security check
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        ProjectResponse updatedProject = projectService.updateProject(userPrincipal.getId(), projectId, request);
        return ResponseEntity.ok(updatedProject);
    }
	
	@PatchMapping("/{projectId}")
    @PreAuthorize("@projectSecurity.isOwnerOrAdmin(#projectId)") // Custom security check
    public ResponseEntity<ProjectResponse> patchProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectPatchRequest request,
            Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        ProjectResponse patchedProject = projectService.patchProject(userPrincipal.getId(), projectId, request);
        return ResponseEntity.ok(patchedProject);
    }
	
	@DeleteMapping("/{projectId}")
    @PreAuthorize("@projectSecurity.isOwnerOrAdmin(#projectId)") // Custom security check
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long projectId,
            Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        projectService.deleteProject(userPrincipal.getId(), projectId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
