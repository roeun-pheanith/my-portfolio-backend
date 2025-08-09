package com.nith.flex.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nith.flex.portfolio.dto.ForgotPasswordRequest;
import com.nith.flex.portfolio.dto.ResetPasswordRequest;
import com.nith.flex.portfolio.helper.SigninRequest;
import com.nith.flex.portfolio.helper.SignupRequest;
import com.nith.flex.portfolio.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signupRequest) {
		System.out.println("User saved: " + signupRequest);
		String token = authService.createUser(signupRequest);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Authorization", "Bearer " + token);
		responseHeaders.set("Access-Control-Expose-Headers", "Authorization");
		return ResponseEntity.ok().headers(responseHeaders).build();
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest signinRequest) {
		System.out.println("User saved: " + signinRequest);
		String token = authService.authenticateUser(signinRequest);
		System.out.println("token : " + token);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Authorization", "Bearer " + token);
		responseHeaders.set("Access-Control-Expose-Headers", "Authorization");
		return ResponseEntity.ok().headers(responseHeaders).build();
	}

	@GetMapping
	public ResponseEntity<?> get() {
		return ResponseEntity.ok("Rest");
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		authService.forgotPassword(request.getEmail());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
		authService.resetPassword(request.getToken(), request.getNewPassword());
		System.out.println("=======================" + request);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/verify-email")
	public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
		authService.verifyEmail(token);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
