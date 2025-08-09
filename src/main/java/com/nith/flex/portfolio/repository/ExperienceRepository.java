package com.nith.flex.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nith.flex.portfolio.entity.Experience;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
	List<Experience> findByUserIdOrderByStartDateDesc(Long userId);

	Optional<Experience> findByIdAndUserId(Long experienceId, Long userId);
}
