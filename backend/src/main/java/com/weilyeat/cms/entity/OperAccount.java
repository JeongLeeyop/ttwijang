package com.ttwijang.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
							
	
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class OperAccount{							
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    // @Comment("회원권한")
    private String auth;
    @Column
    // @Comment("이름")
    private String name;
    @Column
    // @Comment("연락처")
    private String tel;
    @Column
    // @Comment("이메일")
    private String email;
    @Column
    // @Comment("패스워드")
    private String password;
    @Column
    // @Comment("생성일자")
    private String createdDate;
    @Column(length = 30)
    // @Comment("성별")
    private String gender;
    @Column
    // @Comment("생년월일")
    private String birth;
}