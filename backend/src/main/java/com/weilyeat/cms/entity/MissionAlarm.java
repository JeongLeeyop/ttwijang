package com.weilyeat.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mission_alarm")
@Getter @Setter
public class MissionAlarm implements Serializable {
	
	// 게시글 고유값
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;
    private String missionUid;
	private int mon;
	private int tue;
    private int wed;
    private int thu;
    private int fri;
    private int sat;
    private int sun;
	private String alarmTime;
    
	@CreationTimestamp
    private LocalDateTime createDate;
}
