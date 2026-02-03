package com.ttwijang.cms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.ttwijang.cms.common.code.AuthorityCode;

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
// @EntityListeners(AuditingEntityListener.class)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    /* 
     * 1. 일반회원
     * 2. 가맹점 사장
     * 3. 관리자
     */
    @Column
	@Enumerated(EnumType.STRING)
    private AuthorityCode authority;
    
    @Column
    private String name;
    
    @Column
    private LocalDate birth;
    
    @Column(length = 30)
    // @Comment("성별")
    private String gender;
    
    @Column
    // @Comment("연락처")
    private String tel;
    
    @Column(unique = true)
    // @Comment("이메일")
    private String email;
    
    @Column
    // @Comment("패스워드")
    private String password;


    @Column(length = 11)
    // @Comment("우편번호")
    private int postCode;

    @Column
    // @Comment("구주소")
    private String oldAddress;

    @Column
    // @Comment("사용자 주소")
    private String address;

    @Column
    // @Comment("사용자 상세주소")
    private String addressDetail;

    @Column(length = 20)
    // @Comment("주소 시도")
    private String addressSido;

    
    
    @Column
    // @Comment("식단관리유무")
    private String dietCheck;
    
    @Column
    // @Comment("신장")
    private String height;
    
    @Column
    // @Comment("몸무게")
    private String weight;
    
    @Column
    // @Comment("목표몸무게")
    private String destWeight;

    @Column
    // @Comment("목표몸무게 : 다이어리")
    private String destMuscleRate;

    @Column
    // @Comment("목표몸무게 : 다이어리")
    private String destFatRate;
    
    @Column
    // @Comment("목표날짜")
    private LocalDate destDate;
    
    @Column
    // @Comment("활동수준")
    private String activityLevel;
    
    @Column
    // @Comment("식단관리목적")
    private String dietPurpose;
    
    @Column
    // @Comment("식단유의사항")
    private String dietPrecaution;
    
    @Column
    @CreationTimestamp
    // @Comment("생성일자")
    private LocalDateTime createdDate;
    
    @Column
    // @Comment("상태")
    private String status;

    @Column
    // @Comment("탈퇴여부")
    private boolean withdrawStatus;

    @Column
    // @Comment("탈퇴날짜")
    private LocalDateTime withdrawDate;
    
    @Column
    // @Comment("포인트")
    private int point;
    
    // 사이트
	@OneToOne
    // @Comment("썸네일")
	@JoinColumn(name = "thumbnailFileUid")
	private AttachedFile attachedFile;
    //FK
    //id	attachedFileId	썸네일		N


    public Account(Long id, String name, String email, AuthorityCode authority) {
        this.id = id;
    	this.name = name;
        this.email = email;
        this.authority = authority;
    }
/* 
    @Builder
    public Account(Long id, String name, String email, String authority) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.authority = authority;
    } */

    public Account update(String name) {
        this.name = name;
 
        return this;
    }
 
    public String getRoleKey() {
        return "GUEST";
    }
}