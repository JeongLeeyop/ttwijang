package com.weilyeat.cms.entity;

import javax.persistence.Column;
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
@Table(name = "challenge_file")
@Getter @Setter
public class ChallengeFile {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String uid;

	@Column(insertable = false, updatable = false)
	private String challengeUid;

	private String fileUid;
	
	@Enumerated(EnumType.STRING)
	private ChallengeFileType type;
	
	// 순서
	@Column(insertable = false, updatable = false)
	private int viewOrder;
	
	// @ManyToOne
	// @JoinColumn(name = "challengeUid", insertable = false, updatable = false)
	// private Challenge challenge;

	@ManyToOne
	@JoinColumn(name = "fileUid", insertable = false, updatable = false)
	private AttachedFile file;

}
