package com.nith.flex.portfolio.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "project_images") // Good practice to explicitly name tables
public class ProjectImage extends AuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false) // Foreign key to Project
    private Project project;

    @Column(nullable = false, length = 255)
    private String fileName; // Original file name (e.g., "my-image.png")

    @Column(nullable = false, unique = true, length = 255)
    private String storedFileName; // Unique name on the file system (e.g., "uuid_my-image.png")

    @Column(nullable = false)
    private String fileType; // MIME type (e.g., "image/jpeg", "image/png")

    @Column(nullable = false)
    private Long fileSize; // Size in bytes

    @Column(length = 500)
    private String caption; // Your existing field for image description

    private Integer orderIndex; // Your existing field for display order

    @Column(nullable = false)
    private boolean isFeaturedImage = false; // To mark a primary image for the project

}
