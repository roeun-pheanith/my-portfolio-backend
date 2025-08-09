package com.nith.flex.portfolio.helper;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SigninRequest {
	@Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
	private String username;
	
	@Size(max = 100,min = 8, message = "Password must be between 8 and 100 charecters")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,100}$",
    message = "Password must be 8-100 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character (@#$%^&+=!)")
	private String password;
}
