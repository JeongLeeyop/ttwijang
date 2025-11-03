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
@Table(name = "board_use_category")
@Getter @Setter
public class BoardUseCategory {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String uid;

	// 게시판 고유값
	@Column(insertable = false, updatable = false)
	private String boardUid;

	// 카테고리 고유값
	private String categoryUid;
	
	// 순서
	@Column(insertable = false, updatable = false)
	private int viewOrder;

	@ManyToOne
	@JoinColumn(name = "categoryUid", insertable = false, updatable = false)
	private BoardCategory category;
}
