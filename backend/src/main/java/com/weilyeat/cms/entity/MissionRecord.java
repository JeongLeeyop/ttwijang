package com.weilyeat.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mission_record")
@Getter @Setter
public class MissionRecord implements Serializable {
	
	// 게시글 고유값
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;
    
    private String missionUid;

	private String userUid; // 유저
	
	private String writer; // 유저

	private String title; // 챌린지명
    
	private String content; // 내용
    
	private boolean hiddenStatus; // 숨김여부
    
	private boolean deleteStatus; // 삭제여부
        
	@CreationTimestamp
    private LocalDateTime createDate;

	// 이미지 1,2,3
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "missionRecordUid", nullable = false)
	@OrderColumn(name = "viewOrder")
    private List<MissionRecordFile> fileList;
	
	// @Formula("(SELECT COUNT(*) FROM post_like pl WHERE pl.post_uid = uid)")
	// private int ParticipantCnt;
}
