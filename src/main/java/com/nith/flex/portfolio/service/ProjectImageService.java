package com.nith.flex.portfolio.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nith.flex.portfolio.dto.ProjectImageResponse;

public interface ProjectImageService {
    List<ProjectImageResponse> getImagesForProject(Long projectId);
    ProjectImageResponse getImageById(Long projectId, Long imageId); // Authenticated access
    ProjectImageResponse uploadImage(Long userId, Long projectId, MultipartFile file, String caption, Integer orderIndex, Boolean isFeatured);
    ProjectImageResponse updateImageMetadata(Long userId, Long projectId, Long imageId, String caption, Integer orderIndex, Boolean isFeatured); // New method for metadata updates
    void deleteImage(Long userId, Long projectId, Long imageId);
}
