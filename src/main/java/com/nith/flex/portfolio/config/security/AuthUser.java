package com.nith.flex.portfolio.config.security;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class AuthUser implements UserDetails {

	private Long id;
	private String username;
	private String password;
	private Set<? extends GrantedAuthority> authorities;
	private Boolean accountNonExpired;
	private Boolean accountNonLocked;
	private Boolean credentailsNonExpired;
	private Boolean enabled;

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public Set<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	public Boolean getAccountNonExpired() {
		return accountNonExpired;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public Boolean getCredentailsNonExpired() {
		return credentailsNonExpired;
	}

	public Boolean getEnabled() {
		return enabled;
	}
}
