package com.nith.flex.portfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Skill  extends AuditEntity{
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String category;
	private String proficiency;
	private boolean isFeature = false;
	private String iconUrl;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
}
