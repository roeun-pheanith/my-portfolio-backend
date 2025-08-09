package com.nith.flex.portfolio.config.security;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nith.flex.portfolio.entity.Skill;
import com.nith.flex.portfolio.repository.SkillRepository;

import lombok.RequiredArgsConstructor;

@Component("skillSecurity") // Give it a name for SpEL
@RequiredArgsConstructor
public class SkillSecurity {

    private final SkillRepository skillRepository;

    public boolean isOwnerOrAdmin(Long skillId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false; // Not authenticated
        }

        AuthUser currentUser = (AuthUser) authentication.getPrincipal();
        Long currentUserId = currentUser.getId();

        // Check if the current user has ADMIN role (optional, uncomment if needed)
        Set<String> roles = currentUser.getAuthorities().stream()
                            .map(item -> item.getAuthority())
                            .collect(Collectors.toSet());
        if (roles.contains("ROLE_ADMIN")) { // Assuming your roles are prefixed with ROLE_
            return true; // Admin can do anything
        }

        // Check if the current user is the owner of the skill
        Optional<Skill> skillOptional = skillRepository.findById(skillId);
        if (skillOptional.isPresent()) {
            Skill skill = skillOptional.get();
            return skill.getUser().getId().equals(currentUserId);
        }
        return false; // Skill not found or user is not the owner/admin
    }
}
