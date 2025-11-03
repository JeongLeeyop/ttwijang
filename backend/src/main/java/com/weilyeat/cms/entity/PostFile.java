package com.ttwijang.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_file")
@Getter @Setter
public class PostFile {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String uid;

	@Column(insertable = false, updatable = false)
	private String postUid;

	private String fileUid;

	private String fieldUid;
	
	// 순서
	@Column(insertable = false, updatable = false)
	private int viewOrder;
	
	@ManyToOne
	@JoinColumn(name = "postUid", insertable = false, updatable = false)
	private Post post;

	@ManyToOne
	@JoinColumn(name = "fileUid", insertable = false, updatable = false)
	private AttachedFile file;

}
