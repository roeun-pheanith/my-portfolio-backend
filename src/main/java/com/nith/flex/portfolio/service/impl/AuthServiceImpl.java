package com.nith.flex.portfolio.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nith.flex.portfolio.config.security.AuthUser;
import com.nith.flex.portfolio.config.security.JwtUtils;
import com.nith.flex.portfolio.entity.Profile;
import com.nith.flex.portfolio.entity.Role;
import com.nith.flex.portfolio.entity.User;
import com.nith.flex.portfolio.entity.VerificationToken;
import com.nith.flex.portfolio.exception.ApiException;
import com.nith.flex.portfolio.helper.RoleEnum;
import com.nith.flex.portfolio.helper.SigninRequest;
import com.nith.flex.portfolio.helper.SignupRequest;
import com.nith.flex.portfolio.helper.TokenType;
import com.nith.flex.portfolio.repository.ProfileRepository;
import com.nith.flex.portfolio.repository.RoleRepository;
import com.nith.flex.portfolio.repository.UserRepository;
import com.nith.flex.portfolio.repository.VerificationTokenRepository;
import com.nith.flex.portfolio.service.AuthService;
import com.nith.flex.portfolio.service.EmailService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final VerificationTokenRepository verificationTokenRepository;
	private final EmailService emailService;
	private final RoleRepository roleRepository;
	private final ProfileRepository profileRepository;
	private final JwtUtils jwtUtils;

	@Override
	public String createUser(SignupRequest signupRequest) {
		System.out.println(">>> Signup request received for: " + signupRequest.getUsername());
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "Username already token!", "USER_TOKEN");
		}
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "Email already token!", "EMAIL_TOKEN");
		}
		User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
				passwordEncoder.encode(signupRequest.getPassword()));

		Set<String> stringRoles = signupRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (stringRoles == null || Objects.isNull(stringRoles)) {
			Role userRole = roleRepository.findByName(RoleEnum.USER.name())
					.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Role was not found", "ROLE_NOT_FOUND"));
			roles.add(userRole);
		} else {
			stringRoles.forEach(role -> {
				Role adminRole = roleRepository.findByName(role).orElseThrow(
						() -> new ApiException(HttpStatus.NOT_FOUND, "Role was not found", "ROLE_NOT_FOUND"));
				roles.add(adminRole);
			});
		}
		Set<String> savedRoles = roles.stream().map(role -> role.getName()).collect(Collectors.toSet());
		user.setRoles(roles);
		userRepository.save(user);
		
		Profile profile = new Profile();
		profile.setUser(user);
		profileRepository.save(profile);
		

		return jwtUtils.generateJwtToken(signupRequest.getUsername(), savedRoles);
	}

	@Override
	public String authenticateUser(SigninRequest signinRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
		AuthUser userPrincipal = (AuthUser) authentication.getPrincipal();
		Set<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
		return jwtUtils.generateJwtToken(userPrincipal.getUsername(), roles);
	}

	@Override
	@Transactional
	public void forgotPassword(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"Email was not found", "NOT_FOUND"));

		// Delete any existing tokens for this user
		verificationTokenRepository.deleteByUser(user);

		// Create a new password reset token
		String token = UUID.randomUUID().toString();
		VerificationToken resetToken = new VerificationToken(token, TokenType.PASSWORD_RESET, user);
		verificationTokenRepository.save(resetToken);

		// Send the email (using our placeholder service)
		String subject = "Password Reset Request";
		String body = "To reset your password, click the following link:\n"
				+ "http://localhost:8080/api/auth/reset-password?token=" + token; // Adjust URL for your frontend
		emailService.sendEmail(email, subject, body);
	}

	@Override
	@Transactional
	public void resetPassword(String token, String newPassword) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
				.orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Invalid or expired token.", "INVALID_TOKEN"));

		if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			verificationTokenRepository.delete(verificationToken);
			throw new ApiException(HttpStatus.BAD_REQUEST, "Token has expired.", "EXPIRED_TOKEN");
		}

		User user = verificationToken.getUser();
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

		// Invalidate the token after use
		verificationTokenRepository.delete(verificationToken);
	}

	@Override
	@Transactional
	public void verifyEmail(String token) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
				.orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Invalid or expired token.", "INVALID_TOKEN"));

		if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			verificationTokenRepository.delete(verificationToken);
			throw new ApiException(HttpStatus.BAD_REQUEST, "Token has expired.", "EXPIRED_TOKEN");
		}

		User user = verificationToken.getUser();
		user.setEnable(true); // Enable the user after successful verification
		userRepository.save(user);

		// Invalidate the token
		verificationTokenRepository.delete(verificationToken);
	}

}
