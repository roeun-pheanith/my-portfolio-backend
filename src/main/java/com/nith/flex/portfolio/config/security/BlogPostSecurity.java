package com.nith.flex.portfolio.config.security;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nith.flex.portfolio.entity.BlogPost;
import com.nith.flex.portfolio.repository.BlogPostRepository;

import lombok.RequiredArgsConstructor;

@Component("blogPostSecurity")
@RequiredArgsConstructor
public class BlogPostSecurity {

    private final BlogPostRepository blogPostRepository;

    public boolean isOwnerOrAdmin(Long postId) {
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

        Optional<BlogPost> postOptional = blogPostRepository.findById(postId);
        if (postOptional.isPresent()) {
            BlogPost post = postOptional.get();
            return post.getUser().getId().equals(currentUserId);
        }
        return false;
    }
}
