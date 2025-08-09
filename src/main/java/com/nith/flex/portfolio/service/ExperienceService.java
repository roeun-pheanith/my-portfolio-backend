package com.nith.flex.portfolio.service;

import java.util.List;

import com.nith.flex.portfolio.dto.ExperienceRequest;
import com.nith.flex.portfolio.dto.ExperienceResponse;

public interface ExperienceService {
    // Publicly accessible method to get experiences for any user
    List<ExperienceResponse> getExperiencesByUserId(Long userId);

    // Authenticated user methods
    ExperienceResponse getExperienceById(Long experienceId);
    ExperienceResponse createExperience(Long userId, ExperienceRequest request);
    ExperienceResponse updateExperience(Long userId, Long experienceId, ExperienceRequest request);
    void deleteExperience(Long userId, Long experienceId);
}
