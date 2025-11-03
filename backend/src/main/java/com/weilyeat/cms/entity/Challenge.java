package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
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
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "challenge")
@Getter @Setter
public class Challenge implements Serializable {
	
	// 게시글 고유값
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;
    
	private String categoryUid; // 카테고리 아이디

	private String title; // 챌린지명
    
	private String content; // 내용
    
	private String authMethodInfo; // 인증방식
    
	private Integer dailyReward; // 적립금
    
	private LocalDateTime startDate; // 시작일
    
	private LocalDateTime endDate; // 종료일
	
	private Integer totalDay; // 일수
    
	private boolean useStatus; // 숨김여부
    
	private boolean deleteStatus; // 삭제여부

	@CreationTimestamp
    private LocalDateTime createDate;

	@OneToOne
    @JoinColumn(name = "categoryUid", insertable = false, updatable = false)
	private ChallengeCategory challengeCategory;

	// 챌린지 파일 목록
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "challengeUid", nullable = false)
	@OrderColumn(name = "viewOrder")
    private List<ChallengeFile> fileList;
	
	// 챌린지 기록 목록
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "challengeUid", insertable = false, updatable = false)
	@OrderBy("createDate")
	private List<ChallengeRecord> recordList;
	
	// 댓글 목록
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "challengeUid", insertable = false, updatable = false)
	private List<ChallengeUser> challengeUserList;

	@Formula("(SELECT COUNT(*) FROM challenge_user cu WHERE cu.challenge_uid = uid)")
	private int ParticipantCnt;
}
