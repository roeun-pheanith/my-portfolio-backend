package com.nith.flex.portfolio.service;

import com.nith.flex.portfolio.helper.SigninRequest;
import com.nith.flex.portfolio.helper.SignupRequest;

public interface AuthService {
	String createUser(SignupRequest signupRequest);

	String authenticateUser(SigninRequest signinRequest);
	
	void forgotPassword(String email);
	void resetPassword(String token, String newPassword);
	void verifyEmail(String token);
}
