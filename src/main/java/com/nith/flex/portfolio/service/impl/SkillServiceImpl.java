package com.nith.flex.portfolio.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nith.flex.portfolio.dto.SkillPatchRequest;
import com.nith.flex.portfolio.dto.SkillRequest;
import com.nith.flex.portfolio.dto.SkillResponse;
import com.nith.flex.portfolio.entity.Skill;
import com.nith.flex.portfolio.entity.User;
import com.nith.flex.portfolio.exception.ApiException;
import com.nith.flex.portfolio.exception.ResourceException;
import com.nith.flex.portfolio.repository.SkillRepository;
import com.nith.flex.portfolio.repository.UserRepository;
import com.nith.flex.portfolio.service.SkillService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

	private final SkillRepository skillRepository;
	private final UserRepository userRepository;

	// Helper to convert Entity to Response DTO
	private SkillResponse convertToDto(Skill skill) {
		if (skill == null)
			return null;
		SkillResponse dto = new SkillResponse();
		dto.setId(skill.getId());
		dto.setUserId(skill.getUser().getId());
		dto.setUsername(skill.getUser().getUsername());
		dto.setName(skill.getName());
		dto.setProficiency(skill.getProficiency());
		dto.setFeatured(skill.isFeature());
		dto.setCreatedAt(skill.getCreateAt());
		dto.setUpdatedAt(skill.getUpdateAt());
		return dto;
	}

	@Override
	public List<SkillResponse> getAllSkillsByUserId(Long userId) {
		userRepository.findById(userId).orElseThrow(() -> new ResourceException("User", userId, "USER_NOT_FOUND"));
		return skillRepository.findByUserId(userId).stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public SkillResponse getSkillById(Long skillId) {
		Skill skill = skillRepository.findById(skillId)
				.orElseThrow(() -> new ResourceException("Skill", skillId, "SKILL_NOT_FOUND"));
		return convertToDto(skill);
	}

	@Override
	public SkillResponse createSkill(Long userId, SkillRequest request) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceException("User", userId, "USER_NOT_FOUND"));
		if (skillRepository.findByNameAndUserId(request.getName(), userId).isPresent()) {
			throw new ApiException(HttpStatus.CONFLICT,
					"Skill with name %s already exists for this user.".formatted(request.getName()), "NAME_CONFLICT");
		}
		Skill skill = new Skill();
		skill.setUser(user);
		skill.setName(request.getName());
		skill.setProficiency(request.getProficiency());
		skill.setFeature(request.getIsFeatured());

		Skill savedSkill = skillRepository.save(skill);
		return convertToDto(savedSkill);
	}

	@Override
	public SkillResponse updateSkill(Long userId, Long skillId, SkillRequest request) {
		Skill skill = skillRepository.findByIdAndUserId(skillId, userId)
				.orElseThrow(() -> new ResourceException("Skill", skillId, "SKILL_NOT_FOUND"));
		if (!skill.getName().equals(request.getName())) {
			if (skillRepository.findByNameAndUserId(skill.getName(), userId).isPresent()) {
				throw new ApiException(HttpStatus.CONFLICT,
						"Skill with name %s already exists for this user.".formatted(request.getName()),
						"NAME_CONFLICT");
			}
		}
		skill.setName(request.getName());
		skill.setProficiency(request.getProficiency());
		skill.setFeature(request.getIsFeatured());

		Skill updatedSkill = skillRepository.save(skill);
		return convertToDto(updatedSkill);
	}

	@Override
	public SkillResponse patchSkill(Long userId, Long skillId, SkillPatchRequest request) {
		Skill skill = skillRepository.findByIdAndUserId(skillId, userId)
				.orElseThrow(() -> new ResourceException("Skill", skillId, "SKILL_NOT_FOUND"));

		if (request.getName() != null) {
			// Optional: Prevent changing name to an existing skill name for the same user
			if (!skill.getName().equals(request.getName())) {
				if (skillRepository.findByNameAndUserId(request.getName(), userId).isPresent()) {
					throw new ApiException(HttpStatus.CONFLICT,
							"Skill with name %s already exists for this user.".formatted(request.getName()),
							"NAME_CONFLICT");
				}
			}
			skill.setName(request.getName());
		}
		if (request.getProficiency() != null)
			skill.setProficiency(request.getProficiency());
		if (request.getIsFeatured() != null)
			skill.setFeature(request.getIsFeatured());

		Skill patchedSkill = skillRepository.save(skill);
		return convertToDto(patchedSkill);
	}

	@Override
	public void deleteSkill(Long userId, Long skillId) {
		Skill skill = skillRepository.findByIdAndUserId(skillId, userId).orElseThrow(() -> new ResourceException("Skill", skillId, "SKILL_NOT_FOUND"));
		skillRepository.delete(skill);
	}

}
