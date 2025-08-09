package com.nith.flex.portfolio.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
public abstract class AuditEntity {
	@CreatedDate
	@JsonIgnore
	private LocalDateTime createAt;
	@LastModifiedDate
	@JsonIgnore
	private LocalDateTime updateAt;
	@CreatedBy
	@JsonIgnore
	private String userCreate;
	@LastModifiedBy
	@JsonIgnore
	private String userUpdate;
}
