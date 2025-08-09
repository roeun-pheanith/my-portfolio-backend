package com.nith.flex.portfolio.helper;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum RoleEnum {
ADMIN,USER;

public Set<SimpleGrantedAuthority> getAuthrities(){
	Set<SimpleGrantedAuthority> authorities = new HashSet<>();
	SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + this.name());
	authorities.add(authority);
	return authorities;
}
}
