package com.nith.flex.portfolio.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(name = "blog_posts")
public class BlogPost extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200)
	private String title;

	@Column(nullable = false, unique = true, length = 255)
	private String slug;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(columnDefinition = "TEXT")
	private String excerpt;

	private String thumbnailUrl;

	private LocalDateTime publishedAt;

	@Column(nullable = false)
	private Boolean isPublished = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "blog_post_tags", joinColumns = @JoinColumn(name = "blog_post_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<Tag> tags = new HashSet<>();

	@OneToMany(mappedBy = "blogPost", cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<Comment> comments = new HashSet<>();

	public void addTag(Tag tag) {
		this.tags.add(tag);
		tag.getBlogPosts().add(this);
	}

	public void removeTag(Tag tag) {
		this.tags.remove(tag);
		tag.getBlogPosts().remove(this);
	}

	// Helper methods for the OneToMany relationship (comments)
	public void addComment(Comment comment) {
		this.comments.add(comment);
		comment.setBlogPost(this);
	}

	public void removeComment(Comment comment) {
		this.comments.remove(comment);
		comment.setBlogPost(null);
	}
}