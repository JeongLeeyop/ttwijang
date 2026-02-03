package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ttwijang.cms.oauth.soical.SocialType;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    private String siteUid = "00070154-eb1d-4972-97b0-03365762fcc1";

    // 사용자 아이디
    private String userId;

    // 사용자 비밀번호
    private String userPassword;

    // 이름
    private String actualName;

    // 문의 연락처
    private String concatNumber;

    // 제공처
    @Enumerated(EnumType.STRING)
    private SocialType provider;

    // 제공처 사용자 고유값
    private String providerId;

    // 계정 사용여부
    private boolean enabled;

    // 계정 사용여부
    private boolean notLocked;

    // 생년월일
    private LocalDate birth;

    // 이메일
    private String email;

    // 우편번호
    private String postCode;

    // 주소
    private String address;

    // 상세주소
    private String addressDetail;

    private String lat;        
    private String lon;

    // 성별(0: 남자, 1: 여자)
    private Integer gender;

    // 탈퇴여부
    private boolean withdrawStatus; 

    // 가입여부
    private boolean joinStatus;

    // 식단관리 경험 여부
    private boolean dietExperience;

    // 키
    private String height;

    // 체중
    private String weight;

    // 목표 체중
    private String goalWeight;

    // 목표달성 희망일자
    private LocalDate goalDate;

    // 활동수준(1: 비활동적, 2: 저활동적, 3: 활동적, 4: 매우 활동적)
    private Integer activityLevel;

    // 식단관리 목적
    private String dietPurpose;

    // 식단 제공 시 유의사항
    private String dietPrecaution;

    // 마케팅 동의 여부
    private boolean marketingStatus;

    // 상점 고유값(상점관리자 일때)
    private Integer shopIdx;
    
    private Integer stationIdx;

    private Integer point;

    private boolean registerInfoStatus;

    private boolean missionInquiryStatus;

    // 등록일자
    @CreationTimestamp
    private LocalDateTime createDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userUid", insertable = false, updatable = false)
    private List<UserRole> roles;

    public void setUserPassword(String password) {
        if (password == null)
            this.userPassword = "";
        else {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            this.userPassword = passwordEncoder.encode(password);
        }
    }

    public void setOriginalUserPassword(String password) {
        this.userPassword = password;
    }

}