package com.nith.flex.portfolio.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nith.flex.portfolio.dto.ExperienceRequest;
import com.nith.flex.portfolio.dto.ExperienceResponse;
import com.nith.flex.portfolio.entity.Experience;
import com.nith.flex.portfolio.entity.User;
import com.nith.flex.portfolio.exception.ApiException;
import com.nith.flex.portfolio.exception.ResourceException;
import com.nith.flex.portfolio.repository.ExperienceRepository;
import com.nith.flex.portfolio.repository.UserRepository;
import com.nith.flex.portfolio.service.ExperienceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

	private final ExperienceRepository experienceRepository;
	private final UserRepository userRepository;

	private ExperienceResponse convertToDto(Experience experience) {
		if (experience == null)
			return null;
		ExperienceResponse dto = new ExperienceResponse();
		dto.setId(experience.getId());
		dto.setUserId(experience.getUser().getId());
		dto.setUsername(experience.getUser().getUsername());
		dto.setCompany(experience.getCompany());
		dto.setTitle(experience.getTitle());
		dto.setLocation(experience.getLocation());
		dto.setStartDate(experience.getStartDate());
		dto.setEndDate(experience.getEndDate());
		dto.setCurrent(experience.getIsCurrent());
		dto.setDescription(experience.getDescription());
		dto.setType(experience.getType()); // Add the new field
		dto.setCreatedAt(experience.getCreateAt());
		dto.setUpdatedAt(experience.getUpdateAt());
		return dto;
	}

	@Override
	public List<ExperienceResponse> getExperiencesByUserId(Long userId) {
		userRepository.findById(userId).orElseThrow(() -> new ResourceException("User", userId, "USER_NOT_FOUND"));
		return experienceRepository.findByUserIdOrderByStartDateDesc(userId).stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public ExperienceResponse getExperienceById(Long experienceId) {
		Experience experience = experienceRepository.findById(experienceId)
				.orElseThrow(() -> new ResourceException("Experience", experienceId, "EXPERIENCE_NOT_FOUND"));
		return convertToDto(experience);
	}

	@Override
	public ExperienceResponse createExperience(Long userId, ExperienceRequest request) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceException("User", userId, "USER_NOT_FOUND"));

		if (request.getEndDate() != null && request.getEndDate().isBefore(request.getStartDate())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "End date cannot be before start date.",
					"INVALID_TIME_STEMP");
		}
		if (request.getIsCurrent() && request.getEndDate() != null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "End date must be null if the experience is current.",
					"INVALID_TIME_STEMP");
		}

		Experience experience = new Experience();
		experience.setUser(user);
		experience.setCompany(request.getCompany());
		experience.setTitle(request.getTitle());
		experience.setLocation(request.getLocation());
		experience.setStartDate(request.getStartDate());
		experience.setEndDate(request.getEndDate());
		experience.setIsCurrent(request.getIsCurrent());
		experience.setDescription(request.getDescription());
		experience.setType(request.getType()); // Set the new field

		Experience savedExperience = experienceRepository.save(experience);
		return convertToDto(savedExperience);
	}

	@Override
	public ExperienceResponse updateExperience(Long userId, Long experienceId, ExperienceRequest request) {
		Experience experience = experienceRepository.findByIdAndUserId(experienceId, userId)
				.orElseThrow(() -> new ResourceException("Experience", experienceId, "EXPERIENCE_NOT_FOUND"));

		if (request.getEndDate() != null && request.getEndDate().isBefore(request.getStartDate())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "End date cannot be before start date.",
					"INVALID_TIME_STEMP");
		}
		if (request.getIsCurrent() && request.getEndDate() != null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "End date must be null if the experience is current.",
					"INVALID_TIME_STEMP");
		}

		experience.setCompany(request.getCompany());
		experience.setTitle(request.getTitle());
		experience.setLocation(request.getLocation());
		experience.setStartDate(request.getStartDate());
		experience.setEndDate(request.getEndDate());
		experience.setIsCurrent(request.getIsCurrent());
		experience.setDescription(request.getDescription());
		experience.setType(request.getType()); // Update the new field

		Experience updatedExperience = experienceRepository.save(experience);
		return convertToDto(updatedExperience);
	}

	@Override
	public void deleteExperience(Long userId, Long experienceId) {
		Experience experience = experienceRepository.findByIdAndUserId(experienceId, userId)
				.orElseThrow(() -> new ResourceException("Experience", experienceId, "EXPERIENCE_NOT_FOUND"));

		experienceRepository.delete(experience);
	}
}
