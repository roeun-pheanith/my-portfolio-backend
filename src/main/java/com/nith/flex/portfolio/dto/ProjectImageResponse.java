package com.nith.flex.portfolio.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectImageResponse {
    private Long id;
    private Long projectId;
    private String fileName; // Original file name
    private String imageUrl; // Publicly accessible URL for the image
    private long fileSize;
    private String fileType;
    private String caption;
    private Integer orderIndex;
    private boolean isFeaturedImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
