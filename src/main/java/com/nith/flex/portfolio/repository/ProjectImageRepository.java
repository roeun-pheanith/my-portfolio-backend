package com.nith.flex.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nith.flex.portfolio.entity.ProjectImage;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long>{
	List<ProjectImage> findByProjectId(Long projectId);
	Optional<ProjectImage> findByIdAndProjectId(Long imageId, Long projectId);
	Optional<ProjectImage> findByProjectIdAndIsFeaturedImageTrue(Long projectId);
}
