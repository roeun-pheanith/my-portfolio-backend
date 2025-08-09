package com.nith.flex.portfolio.config.security;


import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nith.flex.portfolio.entity.Project;
import com.nith.flex.portfolio.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("projectSecurity")
@RequiredArgsConstructor
public class ProjectSecurity {

    private final ProjectRepository projectRepository;

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    public boolean isOwnerOrAdmin(Long projectId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            log.warn("Unauthorized access attempt to project ID: {}", projectId);
            return false;
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof AuthUser)) {
            log.error("Unexpected principal type: {}", principal.getClass().getName());
            return false;
        }

        AuthUser user = (AuthUser) principal;
        Long userId = user.getId();

        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (roles.contains(ROLE_ADMIN)) {
            log.debug("User {} is ADMIN, granting access to project ID {}", userId, projectId);
            return true;
        }

        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            if (project.getUser() != null && userId.equals(project.getUser().getId())) {
                log.debug("User {} is OWNER of project ID {}", userId, projectId);
                return true;
            } else {
                log.warn("User {} is not owner of project ID {}", userId, projectId);
            }
        } else {
            log.warn("Project ID {} not found", projectId);
        }

        return false;
    }
}