package com.nith.flex.portfolio.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nith.flex.portfolio.config.security.AuthUser;
import com.nith.flex.portfolio.dto.SkillPatchRequest;
import com.nith.flex.portfolio.dto.SkillRequest;
import com.nith.flex.portfolio.dto.SkillResponse;
import com.nith.flex.portfolio.service.SkillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

	private final SkillService skillService;

	@GetMapping("/my")
	@PreAuthorize("permitAll()")
	public ResponseEntity<List<SkillResponse>> getAllSkillsForUser(Authentication authentication) {
		AuthUser user = (AuthUser) authentication.getPrincipal();
		List<SkillResponse> skills = skillService.getAllSkillsByUserId(user.getId());
		System.out.println("----------- Skill -------------: " + skills);
		return ResponseEntity.ok(skills);
	}

	@GetMapping("/{skillId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<SkillResponse> getSkillById(@PathVariable Long skillId) {
		SkillResponse skill = skillService.getSkillById(skillId);
		return ResponseEntity.ok(skill);
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<SkillResponse> createSkill(@Valid @RequestBody SkillRequest request,
			Authentication authentication) {
		AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
		SkillResponse createdSkill = skillService.createSkill(userPrincipal.getId(), request);
		return new ResponseEntity<>(createdSkill, HttpStatus.CREATED);
	}

	@PutMapping("/{skillId}")
	@PreAuthorize("@skillSecurity.isOwnerOrAdmin(#skillId)") // Custom security check
	public ResponseEntity<SkillResponse> updateSkill(@PathVariable Long skillId,
			@Valid @RequestBody SkillRequest request, Authentication authentication) {
		AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
		SkillResponse updatedSkill = skillService.updateSkill(userPrincipal.getId(), skillId, request);
		return ResponseEntity.ok(updatedSkill);
	}

	@PatchMapping("/{skillId}")
	@PreAuthorize("@skillSecurity.isOwnerOrAdmin(#skillId)") // Custom security check
	public ResponseEntity<SkillResponse> patchSkill(@PathVariable Long skillId,
			@Valid @RequestBody SkillPatchRequest request, Authentication authentication) {
		AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
		SkillResponse patchedSkill = skillService.patchSkill(userPrincipal.getId(), skillId, request);
		return ResponseEntity.ok(patchedSkill);
	}

	@DeleteMapping("/{skillId}")
	@PreAuthorize("@skillSecurity.isOwnerOrAdmin(#skillId)") // Custom security check
	public ResponseEntity<Void> deleteSkill(@PathVariable Long skillId, Authentication authentication) {
		AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
		skillService.deleteSkill(userPrincipal.getId(), skillId);
		return ResponseEntity.noContent().build(); // 204 No Content
	}
}
