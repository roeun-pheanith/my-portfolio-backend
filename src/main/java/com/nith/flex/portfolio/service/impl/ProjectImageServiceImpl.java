package com.nith.flex.portfolio.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nith.flex.portfolio.dto.ProjectImageResponse;
import com.nith.flex.portfolio.entity.Project;
import com.nith.flex.portfolio.entity.ProjectImage;
import com.nith.flex.portfolio.exception.ApiException;
import com.nith.flex.portfolio.exception.FileStorageException;
import com.nith.flex.portfolio.exception.ResourceException;
import com.nith.flex.portfolio.helper.FileStorageProperties;
import com.nith.flex.portfolio.repository.ProjectImageRepository;
import com.nith.flex.portfolio.repository.ProjectRepository;
import com.nith.flex.portfolio.service.FileStorageService;
import com.nith.flex.portfolio.service.ProjectImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectImageServiceImpl implements ProjectImageService {

	private final ProjectRepository projectRepository;
	private final ProjectImageRepository projectImageRepository;
	private final FileStorageService fileStorageService;
	private final FileStorageProperties fileStorageProperties;

	// Helper to convert Entity to Response DTO
	private ProjectImageResponse convertToDto(ProjectImage image) {
		if (image == null)
			return null;
		ProjectImageResponse dto = new ProjectImageResponse();
		dto.setId(image.getId());
		dto.setProjectId(image.getProject().getId());
		dto.setFileName(image.getFileName());
		dto.setImageUrl(fileStorageProperties.getDownloadUri() + "/" + image.getStoredFileName());
		dto.setFileSize(image.getFileSize());
		dto.setFileType(image.getFileType());
		dto.setCaption(image.getCaption());
		dto.setOrderIndex(image.getOrderIndex());
		dto.setFeaturedImage(image.isFeaturedImage());
		dto.setCreatedAt(image.getCreateAt());
		dto.setUpdatedAt(image.getUpdateAt());
		return dto;
	}

	@Override
	public List<ProjectImageResponse> getImagesForProject(Long projectId) {
		// You might want to ensure the project itself is publicly accessible here
		// For now, just ensuring it exists.
		projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceException("Project", projectId, "PROJECT_NOT_FOUND"));
		return projectImageRepository.findByProjectId(projectId).stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public ProjectImageResponse getImageById(Long projectId, Long imageId) {
		// This method is for authenticated access to a specific image.
		// Ownership check can be implicit if calling via ProjectImageController's
		// authenticated route.
		ProjectImage image = projectImageRepository.findByIdAndProjectId(imageId, projectId)
				.orElseThrow(() -> new ResourceException("Image", imageId, "IMAGE_NOT_FOUND"));
		return convertToDto(image);
	}

	@Override
	public ProjectImageResponse uploadImage(Long userId, Long projectId, MultipartFile file, String caption,
			Integer orderIndex, Boolean isFeatured) {
		// 1. Validate Project Ownership
		Project project = projectRepository.findByIdAndUserId(projectId, userId)
				.orElseThrow(() -> new ResourceException("Project", projectId, "IMAGE_NOT_FOUND"));

		// 2. Perform basic file validation
		if (file.isEmpty()) {
			throw new FileStorageException("Cannot upload empty file.");
		}
		if (!file.getContentType().startsWith("image/")) {
			throw new FileStorageException("Only image files are allowed.");
		}

		// 3. Store the file on the filesystem
		String storedFileName = fileStorageService.storeFile(file);

		// 4. Handle featured image logic: if this is set to be featured, unset previous
		// one
		if (isFeatured != null && isFeatured) {
			projectImageRepository.findByProjectIdAndIsFeaturedImageTrue(projectId).ifPresent(existingFeatured -> {
				existingFeatured.setFeaturedImage(false);
				projectImageRepository.save(existingFeatured);
			});
		}

		// 5. Save metadata to the database
		ProjectImage projectImage = new ProjectImage();
		projectImage.setProject(project);
		projectImage.setFileName(file.getOriginalFilename());
		projectImage.setStoredFileName(storedFileName);
		projectImage.setFileSize(file.getSize());
		projectImage.setFileType(file.getContentType());
		projectImage.setCaption(caption);
		projectImage.setOrderIndex(orderIndex);
		projectImage.setFeaturedImage(isFeatured != null ? isFeatured : false);

		ProjectImage savedImage = projectImageRepository.save(projectImage);
		return convertToDto(savedImage);
	}

	@Override
	public ProjectImageResponse updateImageMetadata(Long userId, Long projectId, Long imageId, String caption,
			Integer orderIndex, Boolean isFeatured) {
		// 1. Validate Image existence and Project Ownership (via findByIdAndProjectId)
		ProjectImage image = projectImageRepository.findByIdAndProjectId(imageId, projectId)
				.orElseThrow(() -> new ResourceException("Image", imageId,"IMAGE_NOT_FOUND"));

		// Redundant check, but ensures the project linked to image is also owned by
		// user
		if (!image.getProject().getUser().getId().equals(userId)) {
			throw new ApiException(HttpStatus.FORBIDDEN, "Access Denied: You do not own this project's image.", "FORBIDDEN");
		}

		// 2. Apply updates only if provided (null checks)
		Optional.ofNullable(caption).ifPresent(image::setCaption);
		Optional.ofNullable(orderIndex).ifPresent(image::setOrderIndex);

		if (isFeatured != null) {
			if (isFeatured) {
				// Unset previous featured image for this project if setting a new one
				projectImageRepository.findByProjectIdAndIsFeaturedImageTrue(projectId).ifPresent(existingFeatured -> {
					if (!existingFeatured.getId().equals(imageId)) { // Don't unset self if already featured
						existingFeatured.setFeaturedImage(false);
						projectImageRepository.save(existingFeatured);
					}
				});
			}
			image.setFeaturedImage(isFeatured);
		}

		ProjectImage updatedImage = projectImageRepository.save(image);
		return convertToDto(updatedImage);
	}

	@Override
	public void deleteImage(Long userId, Long projectId, Long imageId) {
		// 1. Validate Image existence and Project Ownership
		ProjectImage image = projectImageRepository.findByIdAndProjectId(imageId, projectId)
				.orElseThrow(() -> new ResourceException("Image", imageId,"IMAGE_NOT_FOUND"));

		// Redundant check, but ensures the project linked to image is also owned by
		// user
		if (!image.getProject().getUser().getId().equals(userId)) {
			throw new ApiException(HttpStatus.FORBIDDEN, "Access Denied: You do not own this project's image.", "FOBIDDEN");
		}

		// 2. Delete file from filesystem
		fileStorageService.deleteFile(image.getStoredFileName());

		// 3. Delete metadata from database
		projectImageRepository.delete(image);
	}
}