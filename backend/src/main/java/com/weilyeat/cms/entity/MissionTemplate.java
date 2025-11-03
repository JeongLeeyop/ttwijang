package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mission_template")
@Getter @Setter
public class MissionTemplate implements Serializable {
	
	// 게시글 고유값
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;
    
	private String categoryUid; // 카테고리 아이디

	private String title; // 미션명
    
	private String content; // 내용
    
	private String authMethodInfo; // 인증방식
    
	private boolean useStatus; // 숨김여부
    
	@CreationTimestamp
    private LocalDateTime createDate;

	@OneToOne
    @JoinColumn(name = "categoryUid", insertable = false, updatable = false)
	private MissionCategory missionCategory;

	// @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	// @JoinColumn(name = "templateUid", nullable = false)
	// @OrderColumn(name = "viewOrder")
    // private List<MissionFile> fileList;	

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "templateUid")
	@OrderBy("viewOrder ASC")
    private List<MissionFile> fileList =  new ArrayList<MissionFile>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "templateUid", insertable = false, updatable = false)
	private List<Mission> missionList;
}
