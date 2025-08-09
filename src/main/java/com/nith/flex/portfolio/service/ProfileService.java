package com.nith.flex.portfolio.service;

import com.nith.flex.portfolio.dto.ProfilePatchRequest;
import com.nith.flex.portfolio.dto.ProfileRequest;
import com.nith.flex.portfolio.dto.ProfileResponse;

public interface ProfileService {
	ProfileResponse getMyProfile(Long userId);
	ProfileResponse getProfileById(Long profileId);
	ProfileResponse updateMyProfile(Long userId, ProfileRequest request);
	ProfileResponse patchMyProfile(Long userId, ProfilePatchRequest request);
}
