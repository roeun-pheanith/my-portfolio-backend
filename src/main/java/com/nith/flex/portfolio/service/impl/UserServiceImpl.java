package com.nith.flex.portfolio.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nith.flex.portfolio.config.security.AuthUser;
import com.nith.flex.portfolio.entity.Role;
import com.nith.flex.portfolio.entity.User;
import com.nith.flex.portfolio.repository.UserRepository;
import com.nith.flex.portfolio.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional<AuthUser> findUserByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("This username was not found"));
		AuthUser authUser = AuthUser.builder().id(user.getId()).username(user.getUsername()).password(user.getPassword())
				.accountNonExpired(user.getAccountNonExpired()).accountNonLocked(user.getAccountNonLocked())
				.credentailsNonExpired(user.getCredentialsNonExpried()).enabled(user.getEnable())
				.authorities(getAuthorities(user.getRoles())).build();

		return Optional.ofNullable(authUser);
	}

	private Set<SimpleGrantedAuthority> getAuthorities(Set<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
				.collect(Collectors.toSet());
	}

}
