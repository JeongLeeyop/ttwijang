package com.ttwijang.cms.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mission_file")
@Getter @Setter
public class MissionFile {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String uid;

	private String missionUid;

	private String templateUid;

	private String fileUid;
	
	@Enumerated(EnumType.STRING)
	private MissionFileType type;
	
	// 순서
	private int viewOrder;
	
	// @ManyToOne
	// @JoinColumn(name = "missionUid", insertable = false, updatable = false)
	// private Mission mission;

	@ManyToOne
	@JoinColumn(name = "fileUid", insertable = false, updatable = false)
	private AttachedFile file;

}
