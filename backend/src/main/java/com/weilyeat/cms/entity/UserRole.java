package com.weilyeat.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String uid;

	private String userUid;

	private String roleCode;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "roleCode", insertable = false, updatable = false)
	private Role role;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userUid", insertable = false, updatable = false)
	private User user;

	@CreationTimestamp
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
	@Column(name = "create_date")
	private LocalDateTime createDate;

	public UserRole(String uid, String userUid, Role role, LocalDateTime createDate) {
		this.uid = uid;
		this.userUid = userUid;
		this.role = role;
		this.createDate = createDate;
	}

}