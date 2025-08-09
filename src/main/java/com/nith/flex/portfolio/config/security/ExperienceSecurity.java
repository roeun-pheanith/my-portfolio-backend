package com.nith.flex.portfolio.config.security;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nith.flex.portfolio.entity.Experience;
import com.nith.flex.portfolio.repository.ExperienceRepository;

import lombok.RequiredArgsConstructor;

@Component("experienceSecurity")
@RequiredArgsConstructor
public class ExperienceSecurity {

    private final ExperienceRepository experienceRepository;

    public boolean isOwnerOrAdmin(Long experienceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        AuthUser currentUser = (AuthUser) authentication.getPrincipal();
        Long currentUserId = currentUser.getId();

        Set<String> roles = currentUser.getAuthorities().stream()
                            .map(item -> item.getAuthority())
                            .collect(Collectors.toSet());
        if (roles.contains("ROLE_ADMIN")) {
            return true;
        }

        Optional<Experience> experienceOptional = experienceRepository.findById(experienceId);
        if (experienceOptional.isPresent()) {
            Experience experience = experienceOptional.get();
            return experience.getUser().getId().equals(currentUserId);
        }
        return false;
    }
}
