package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 팀 엔티티
 * 풋살 팀의 기본 정보 및 설정을 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "team")
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 팀 이름
    @Column(nullable = false, length = 100)
    private String name;

    // 팀 코드 (URL 슬러그 및 초대 코드)
    @Column(nullable = false, unique = true, length = 20)
    private String teamCode;

    // 팀 로고 이미지 UID
    private String logoFileUid;

    // 팀 소개
    @Column(columnDefinition = "TEXT")
    private String description;

    // 후원 계좌 은행명
    private String bankName;

    // 후원 계좌 번호
    private String bankAccount;

    // 환불 계좌 은행명
    private String refundBankName;

    // 환불 계좌 번호
    private String refundBankAccount;

    // 활동 요일 (비트마스크: 일=1, 월=2, 화=4, 수=8, 목=16, 금=32, 토=64)
    private Integer activeDays;

    // 활동 시간대 (비트마스크: 아침=1, 낮=2, 저녁=4, 심야=8)
    private Integer activeTimeSlots;

    // 성별 (0: 남자, 1: 여자, 2: 혼성)
    private Integer genderType;

    // 연령대 (비트마스크: 10대=1, 20대=2, 30대=4, 40대=8, 50대=16, 60대=32)
    private Integer ageGroups;

    // 활동 지역 - 도/시
    private String regionSido;

    // 활동 지역 - 시/군/구
    private String regionSigungu;

    // 총 후원 금액
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer totalSponsorship;

    // 홈 구장명
    private String homeStadium;

    // 홈 구장 주소
    private String homeStadiumAddress;

    // 매너 점수 (평균)
    @Column(columnDefinition = "DECIMAL(3,2) DEFAULT 0.0")
    private Double mannerScore;

    // 월 회비
    private Integer monthlyFee;

    // 팀 운영자 UID
    @Column(nullable = false)
    private String ownerUid;

    // 구단주 UID
    private String sponsorOwnerUid;

    // 회원 수
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer memberCount;

    // 팀 상태 (ACTIVE, INACTIVE, DELETED)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TeamStatus status;

    // 회원 모집 중 여부
    private Boolean recruitingMembers;

    // 팀 특징 태그 (JSON 형태로 저장)
    @Column(columnDefinition = "TEXT")
    private String featureTags;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    private LocalDateTime deletedDate;

    // 팀 멤버 관계
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TeamMember> members;

    // 팀이 참가한 리그 관계
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LeagueTeam> leagueTeams;

    public enum TeamStatus {
        ACTIVE, INACTIVE, DELETED
    }
}
