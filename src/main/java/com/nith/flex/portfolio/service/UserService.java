package com.nith.flex.portfolio.service;

import java.util.Optional;

import com.nith.flex.portfolio.config.security.AuthUser;

public interface UserService {
	Optional<AuthUser> findUserByUsername(String username);
}
