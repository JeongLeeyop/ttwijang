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
@Table(name = "mission_record_file")
@Getter @Setter
public class MissionRecordFile {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String uid;

	@Column(insertable = false, updatable = false)
	private String missionRecordUid;

	private String fileUid;
	
	// 순서
	@Column(insertable = false, updatable = false)
	private int viewOrder;
	
	// @ManyToOne
	// @JoinColumn(name = "missionRecordUid", insertable = false, updatable = false)
	// private MissionRecord missionRecord;

	@ManyToOne
	@JoinColumn(name = "fileUid", insertable = false, updatable = false)
	private AttachedFile file;

}
