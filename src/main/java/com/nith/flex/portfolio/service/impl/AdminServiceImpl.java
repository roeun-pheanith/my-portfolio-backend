package com.nith.flex.portfolio.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nith.flex.portfolio.dto.UserResponse;
import com.nith.flex.portfolio.dto.UserRoleUpdateRequest;
import com.nith.flex.portfolio.entity.Role;
import com.nith.flex.portfolio.entity.User;
import com.nith.flex.portfolio.exception.ApiException;
import com.nith.flex.portfolio.exception.ResourceException;
import com.nith.flex.portfolio.repository.RoleRepository;
import com.nith.flex.portfolio.repository.UserRepository;
import com.nith.flex.portfolio.service.AdminService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final ProfileServiceImpl userProfileService; // Assuming you have a helper for DTO conversion

	public UserResponse convertToDto(User user) {
		if (user == null) {
			return null;
		}
		UserResponse dto = new UserResponse();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setRoles(user.getRoles().stream().map(roles -> roles.getName()).collect(Collectors.toSet()));
		
		// Add any other user profile fields you have
		// dto.setFirstName(user.getFirstName());
		// dto.setLastName(user.getLastName());
		// dto.setProfileImageUrl(user.getProfileImageUrl());

		// Map the new fields
		dto.setIsEnabled(user.getEnable());
		dto.setCreatedAt(user.getCreatedAt());
		dto.setUpdatedAt(user.getUpdatedAt());

		return dto;
	}

	@Override
	public List<UserResponse> getAllUsers() {
		List<User> all = userRepository.findAll();
		System.out.println("-------- user model ------- : " + all);
		List<UserResponse> users = userRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
		System.out.println("-------- user reponse ------- : " + users);
		return users;
	}

	@Override
	@Transactional
	public UserResponse updateUserRole(Long userId, UserRoleUpdateRequest request) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceException("User", userId, "NOT_FOUND"));

		Role newRole = roleRepository.findByName(request.getNewRole()).orElseThrow(
				() -> new ApiException(HttpStatus.NOT_FOUND, "Role with name = %s was not found.", "NOT_FOUND"));

		if (user.getRoles().contains(newRole)) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "User already has this role.", "DUPLICATE_ROLE");
		}

		user.getRoles().clear();
		user.getRoles().add(newRole);

		User updatedUser = userRepository.save(user);
		return convertToDto(updatedUser);
	}

	@Override
	public void disableUser(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceException("User", userId, "NOT_FOUND"));
		user.setEnable(false);
		userRepository.save(user);
	}

	@Override
	public void enableUser(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceException("User", userId, "NOT_FOUND"));
		user.setEnable(true);
		userRepository.save(user);
	}

	@Override
	public void deleteUser(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceException("User", userId, "NOT_FOUND"));
		userRepository.delete(user);
	}
}
