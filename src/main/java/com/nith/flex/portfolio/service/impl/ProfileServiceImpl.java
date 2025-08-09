package com.nith.flex.portfolio.service.impl;

import org.springframework.stereotype.Service;

import com.nith.flex.portfolio.dto.ProfilePatchRequest;
import com.nith.flex.portfolio.dto.ProfileRequest;
import com.nith.flex.portfolio.dto.ProfileResponse;
import com.nith.flex.portfolio.entity.Profile;
import com.nith.flex.portfolio.entity.User;
import com.nith.flex.portfolio.exception.ApiException;
import com.nith.flex.portfolio.exception.ResourceException;
import com.nith.flex.portfolio.repository.ProfileRepository;
import com.nith.flex.portfolio.repository.UserRepository;
import com.nith.flex.portfolio.service.ProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;

	private final UserRepository userRepository;

	public ProfileResponse convertToDto(Profile profile) {
		if (profile == null)
			return null;
		ProfileResponse dto = new ProfileResponse();
		dto.setId(profile.getId());
		dto.setUserId(profile.getUser().getId());
		dto.setUsername(profile.getUser().getUsername());
		// dto.setEmail(profile.getUser().getEmail()); // Include if email is public
		dto.setBio(profile.getBio());
		dto.setHeadline(profile.getHeadline());
		dto.setProfilePictureUrl(profile.getProfilePictureUrl());
		dto.setLinkedinUrl(profile.getLinkedinUrl());
		dto.setGithubUrl(profile.getGithubUrl());
		dto.setLocation(profile.getLocation());
		dto.setPhoneNumber(profile.getPhoneNumber());
		dto.setCreateAt(profile.getCreateAt());
        dto.setUpdateAt(profile.getUpdateAt());
		return dto;
	}

	@Override
	public ProfileResponse getMyProfile(Long userId) {
		Profile profile = profileRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceException("User", userId, "USER_NOT_FOUND"));
		return convertToDto(profile);
	}

	@Override
	public ProfileResponse getProfileById(Long profileId) {
		Profile profile = profileRepository.findById(profileId)
				.orElseThrow(() -> new ResourceException("Profile", profileId, "PROFILE_NOT_FOUND"));
		return convertToDto(profile);
	}

	@Override
	public ProfileResponse updateMyProfile(Long userId, ProfileRequest request) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceException("User", userId, "USER_NOT_FOUND"));
		Profile profile = profileRepository.findByUserId(userId).orElseGet(() -> {
			Profile newProfile = new Profile();
			newProfile.setUser(user);
			return newProfile;
		});
		profile.setBio(request.getBio());
		profile.setHeadline(request.getHeadline());
		profile.setProfilePictureUrl(request.getProfilePictureUrl());
		profile.setLinkedinUrl(request.getLinkedinUrl());
		profile.setGithubUrl(request.getGithubUrl());
		profile.setLocation(request.getLocation());
		profile.setPhoneNumber(request.getPhoneNumber());

		Profile savedProfile = profileRepository.save(profile);
		return convertToDto(savedProfile);
	}

	@Override
	public ProfileResponse patchMyProfile(Long userId, ProfilePatchRequest request) {
		Profile profile = profileRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceException("Profile", userId, "PROFILE_NOT_FOUND"));
		if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getHeadline() != null) profile.setHeadline(request.getHeadline());
        if (request.getProfilePictureUrl() != null) profile.setProfilePictureUrl(request.getProfilePictureUrl());
        if (request.getLinkedinUrl() != null) profile.setLinkedinUrl(request.getLinkedinUrl());
        if (request.getGithubUrl() != null) profile.setGithubUrl(request.getGithubUrl());
        if (request.getLocation() != null) profile.setLocation(request.getLocation());
        if (request.getPhoneNumber() != null) profile.setPhoneNumber(request.getPhoneNumber());

        Profile savedProfile = profileRepository.save(profile);
        return convertToDto(savedProfile);
	}

}
