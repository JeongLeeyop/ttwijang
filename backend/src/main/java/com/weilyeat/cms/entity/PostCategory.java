package com.weilyeat.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_category")
@Getter @Setter
public class PostCategory {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String uid;

	@Column(insertable = false, updatable = false)
	private String postUid;
	
	private String categoryUid;
	
	@ManyToOne
	@JoinColumn(name = "postUid", insertable = false, updatable = false)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryUid", insertable = false, updatable = false)
	private BoardCategory category;

}
