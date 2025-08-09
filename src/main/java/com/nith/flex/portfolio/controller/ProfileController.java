package com.nith.flex.portfolio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nith.flex.portfolio.config.security.AuthUser;
import com.nith.flex.portfolio.dto.ProfilePatchRequest;
import com.nith.flex.portfolio.dto.ProfileRequest;
import com.nith.flex.portfolio.dto.ProfileResponse;
import com.nith.flex.portfolio.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;

	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getMyProfile(Authentication authentication) {
	    System.out.println("Authentication = " + authentication);
	    AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
	    System.out.println("User ID = " + userPrincipal.getId());
	    
	    ProfileResponse profile = profileService.getMyProfile(userPrincipal.getId());
	    return ResponseEntity.ok(profile);
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getProfileById(@PathVariable Long id) {
		ProfileResponse profile = profileService.getProfileById(id);
		return ResponseEntity.ok(profile);
	}

	@PutMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> updateMyProfile(@Valid @RequestBody ProfileRequest profileRequest,
			Authentication authentication) {
		AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
		ProfileResponse profile = profileService.updateMyProfile(userPrincipal.getId(), profileRequest);
		return ResponseEntity.ok(profile);
	}

	@PatchMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> patchMyProfile(@Valid @RequestBody ProfilePatchRequest request,
			Authentication authentication) {
		AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
		ProfileResponse profile = profileService.patchMyProfile(userPrincipal.getId(), request);
		return ResponseEntity.ok(profile);
	}
}
