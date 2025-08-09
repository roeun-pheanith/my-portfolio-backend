package com.nith.flex.portfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nith.flex.portfolio.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
	// Find all skills for a specific user
    List<Skill> findByUserId(Long userId); // Use List as skills are often few per user, or Page if many.

    // Find a specific skill by its ID and the user's ID (for ownership check)
    Optional<Skill> findByIdAndUserId(Long skillId, Long userId);

    // Optional: find by name and user to prevent duplicate skills for same user
    Optional<Skill> findByNameAndUserId(String name, Long userId);
}
