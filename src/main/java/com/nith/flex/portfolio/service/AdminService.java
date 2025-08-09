package com.nith.flex.portfolio.service;

import java.util.List;

import com.nith.flex.portfolio.dto.UserResponse;
import com.nith.flex.portfolio.dto.UserRoleUpdateRequest;

public interface AdminService {
    List<UserResponse> getAllUsers();
    UserResponse updateUserRole(Long userId, UserRoleUpdateRequest request);
    void disableUser(Long userId);
    void enableUser(Long userId);
    void deleteUser(Long userId); // Hard delete, use with caution
}
