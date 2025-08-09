package com.nith.flex.portfolio.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nith.flex.portfolio.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>{
	// Find all projects for a specific user, with pagination
    Page<Project> findByUserId(Long userId, Pageable pageable);

    // Find a specific project by its ID and the user's ID (for ownership check)
    Optional<Project> findByIdAndUserId(Long projectId, Long userId);

    // Find all projects marked as featured, with pagination
    Page<Project> findByIsFeatureTrue(Pageable pageable);
}
