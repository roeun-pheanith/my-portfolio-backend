package com.nith.flex.portfolio.service;

import java.util.List;

import com.nith.flex.portfolio.dto.SkillPatchRequest;
import com.nith.flex.portfolio.dto.SkillRequest;
import com.nith.flex.portfolio.dto.SkillResponse;

public interface SkillService {
    // Publicly accessible methods
    List<SkillResponse> getAllSkillsByUserId(Long userId); // Get all skills for a specific user

    // User-specific (authenticated) methods
    SkillResponse getSkillById(Long skillId); // For authenticated user (implicitly owned) or admin
    SkillResponse createSkill(Long userId, SkillRequest request);
    SkillResponse updateSkill(Long userId, Long skillId, SkillRequest request);
    SkillResponse patchSkill(Long userId, Long skillId, SkillPatchRequest request);
    void deleteSkill(Long userId, Long skillId);
}
