package com.weilyeat.cms.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "board")
@Getter @Setter
public class Board {
	
	// 게시판 고유값
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;
	
	// 게시판 스킨
	private String skin;
	
	// 게시판 명
	private String name;
	
	// 첨부파일 갯수 제한
	private int fileCountLimit;
	
	// 첨부파일 용량 제한(MB)
	private int fileSizeLimit;
	
	// 기본 첨부파일 사용 여부
	private boolean fileUseState;
	
	// 페이지당 보여줄 개수
	private int listSize;
	
	// 목록/읽기 권한
	@Enumerated(EnumType.STRING)
	private BoardAuth authRead;
	
	// 작성/수정 권한
	@Enumerated(EnumType.STRING)
	private BoardAuth authWrite;
	
	// 답글 권한
	@Enumerated(EnumType.STRING)
	private BoardAuth authReply;
	
	// 댓글 권한
	@Enumerated(EnumType.STRING)
	private BoardAuth authComment;
	
	// 비공개 여부
	private boolean privateState;
	
	// 답글 허용 여부
	private boolean replyState;
	
	// 댓글 허용 여부
	private boolean commentState;
	
	// 비밀글 허용 여부
	private boolean secretState;
	
	// 공지글 허용 여부
	private boolean noticeState;
	
	// 생성일 
    @CreationTimestamp
    private LocalDateTime createDate;

	@ManyToOne
	@JoinColumn(name = "skin", insertable = false, updatable = false)
	private BoardSkin boardSkin;
    
    // 게시판 필드 목록
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "boardUid", nullable = false)
    @OrderColumn(name = "viewOrder")
	private List<BoardField> fieldList;
    
    // 게시판 카테고리 목록
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "boardUid", nullable = false)
    @OrderColumn(name = "viewOrder")
	private List<BoardUseCategory> categoryList;
    
    // 게시판 권한 목록
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "boardUid", nullable = false)
    private List<BoardRole> roleList;
    
}
