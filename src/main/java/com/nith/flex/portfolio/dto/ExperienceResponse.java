package com.nith.flex.portfolio.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.nith.flex.portfolio.helper.ExperienceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceResponse {
    private Long id;
    private Long userId;
    private String username;

    private String company;
    private String title;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCurrent;
    private String description;
    private ExperienceType type;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
