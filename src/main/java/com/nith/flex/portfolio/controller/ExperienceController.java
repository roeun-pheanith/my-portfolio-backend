package com.nith.flex.portfolio.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nith.flex.portfolio.config.security.AuthUser;
import com.nith.flex.portfolio.dto.ExperienceRequest;
import com.nith.flex.portfolio.dto.ExperienceResponse;
import com.nith.flex.portfolio.service.ExperienceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    // --- Public Endpoints ---

    @GetMapping("/my")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ExperienceResponse>> getExperiencesByUserId(Authentication authentication) {
    	AuthUser user = (AuthUser) authentication.getPrincipal();
        List<ExperienceResponse> experiences = experienceService.getExperiencesByUserId(user.getId());
        return ResponseEntity.ok(experiences);
    }

    // --- Authenticated User Endpoints ---

    @GetMapping("/{experienceId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExperienceResponse> getExperienceById(@PathVariable Long experienceId) {
        ExperienceResponse experience = experienceService.getExperienceById(experienceId);
        return ResponseEntity.ok(experience);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExperienceResponse> createExperience(
            @Valid @RequestBody ExperienceRequest request,
            Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        ExperienceResponse createdExperience = experienceService.createExperience(userPrincipal.getId(), request);
        return new ResponseEntity<>(createdExperience, HttpStatus.CREATED);
    }

    @PutMapping("/{experienceId}")
    @PreAuthorize("@experienceSecurity.isOwnerOrAdmin(#experienceId)")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @PathVariable Long experienceId,
            @Valid @RequestBody ExperienceRequest request,
            Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        ExperienceResponse updatedExperience = experienceService.updateExperience(userPrincipal.getId(), experienceId, request);
        return ResponseEntity.ok(updatedExperience);
    }

    @DeleteMapping("/{experienceId}")
    @PreAuthorize("@experienceSecurity.isOwnerOrAdmin(#experienceId)")
    public ResponseEntity<Void> deleteExperience(
            @PathVariable Long experienceId,
            Authentication authentication) {
        AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
        experienceService.deleteExperience(userPrincipal.getId(), experienceId);
        return ResponseEntity.noContent().build();
    }
}
