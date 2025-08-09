package com.nith.flex.portfolio.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private Set<String> roles;
    private Boolean isEnabled; // New field
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // ...
}
