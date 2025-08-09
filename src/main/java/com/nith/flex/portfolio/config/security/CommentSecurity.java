package com.nith.flex.portfolio.config.security;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nith.flex.portfolio.entity.Comment;
import com.nith.flex.portfolio.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Component("commentSecurity")
@RequiredArgsConstructor
public class CommentSecurity {

    private final CommentRepository commentRepository;

    public boolean isOwnerOrAdmin(Long commentId) {
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

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            // Check if the user ID is not null (i.e., not an anonymous comment)
            // and if the current user is the owner.
            return comment.getUser() != null && comment.getUser().getId().equals(currentUserId);
        }
        return false;
    }
}
