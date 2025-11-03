package com.weilyeat.cms.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "mission")
@Getter @Setter
public class Mission implements Serializable {
	
	// 게시글 고유값
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;
    
	private String categoryUid; // 카테고리 아이디

	@Column(nullable = true)
	private String templateUid; // 템플릿 아이디

	private String title; // 미션명
    
	private String content; // 내용
    
	private String authMethodInfo; // 인증방식
    
	private Integer dailyReward; // 적립금

	private String reward;
    
	private LocalDate startDate; // 시작일
    
	private LocalDate endDate; // 종료일
	
	private Integer totalDay; // 일수
    
	private boolean useStatus; // 숨김여부
    
	private boolean deleteStatus; // 삭제여부

	private Integer resultStatus; // 결과여부
	
	private Double achieveEffect; // 감량효과

	private boolean alarmStatus;
	private boolean mon;
	private boolean tue;
    private boolean wed;
    private boolean thu;
    private boolean fri;
    private boolean sat;
    private boolean sun;
	private String alarmTime;
	private String alarmTitle;
	private String alarmMessage;

	private String userUid; // 사용자 아이디

	@CreationTimestamp
    private LocalDateTime createDate;

	@OneToOne
    @JoinColumn(name = "categoryUid", insertable = false, updatable = false)
	private MissionCategory missionCategory;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "missionUid")
	@OrderBy(value = "viewOrder ASC")
    private List<MissionFile> fileList =  new ArrayList<MissionFile>();
	
	// @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	// @JoinColumn(name = "missionUid", nullable = false)
    // private List<MissionAlarm> alarmList;

	// @ManyToOne
    // @JoinColumn(name ="templateUid", insertable = false, updatable = false, nullable = true)
    // private MissionTemplate template;

	// @ManyToOne
    // @JoinColumn(name ="userUid", insertable = false, updatable = false)
    // private User user;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "missionUid", insertable = false, updatable = false)
	@OrderBy("createDate DESC")
	private List<MissionRecord> recordList;
	
	// 참여 목록
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "missionUid", insertable = false, updatable = false)
	private List<MissionUser> missionUserList;

	@Formula("(SELECT COUNT(*) FROM mission_user cu WHERE cu.mission_uid = uid)")
	private int ParticipantCnt;
}
