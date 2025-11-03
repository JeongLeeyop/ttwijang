package com.weilyeat.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "challenge_user")
@Getter @Setter
public class ChallengeUser implements Serializable {
	
	// 게시글 고유값
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;
	private String challengeUid; // 챌린지 아이디
	private String userUid; // 카테고리 아이디
	@CreationTimestamp
    private LocalDateTime startDate;
	private LocalDateTime dueDate; // 종료일

	@ManyToOne
    @JoinColumn(name ="challengeUid", insertable = false, updatable = false)
    private Challenge challenge;
	
	@ManyToOne
	@JoinColumn(name ="userUid", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private User user;
}
