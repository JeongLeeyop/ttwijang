package com.weilyeat.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "board_role")
@Getter @Setter
public class BoardRole {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String uid;

	// 게시판 고유값
	@Column(insertable = false, updatable = false)
	private String boardUid;

	// 권한 코드
	private String roleCode;
	
	// 기능
	private String action;
	
}
