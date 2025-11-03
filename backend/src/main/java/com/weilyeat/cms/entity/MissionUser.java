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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mission_user")
@Getter @Setter
public class MissionUser implements Serializable {
	
	// 게시글 고유값
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;
	private String missionUid; // 미션 아이디
	private String userUid; // 카테고리 아이디
	@CreationTimestamp
    private LocalDateTime startDate;
	private LocalDateTime dueDate; // 종료일
	private boolean approveStatus; // 승인여부
	private boolean abandonStatus; // 포기여부 (true: 포기함, false: 포기하지 않음)

	@ManyToOne
    @JoinColumn(name ="missionUid", insertable = false, updatable = false)
    private Mission mission;
	
	@ManyToOne
    @JoinColumn(name ="userUid", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

	@Formula("(SELECT COUNT(*) FROM mission_record cu WHERE cu.mission_uid = mission_uid AND cu.user_uid = user_uid)")
	private int RecordCnt;

	@Formula("(SELECT COUNT(*) FROM mission_record mr WHERE mr.mission_uid = mission_uid AND mr.user_uid = user_uid AND DATE(mr.create_date) = CURDATE())")
	private int todayWriteStatus;
}
