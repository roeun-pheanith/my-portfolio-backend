package com.nith.flex.portfolio.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.nith.flex.portfolio.service.UserService;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AuthUser auth = userService.findUserByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
		System.out.println("auth user : " + auth);
		return auth;

	}

}
