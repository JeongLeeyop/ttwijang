package com.ttwijang.cms.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRolePk implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String userUid;
	@ManyToOne
	@JoinColumn(name = "role_code")
	private Role role;
}