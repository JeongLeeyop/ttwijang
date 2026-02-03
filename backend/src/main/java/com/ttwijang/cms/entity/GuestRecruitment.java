package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
 * 게스트 모집 엔티티
 * 용병 모집 정보 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "guest_recruitment")
public class GuestRecruitment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 모집 팀 UID
    @Column(nullable = false)
    private String teamUid;

    // 관련 매치 UID (선택사항)
    private String matchUid;

    // 경기 일자 (7일 이내만 모집 가능)
    @Column(nullable = false)
    private LocalDate matchDate;

    // 경기 시작 시간
    @Column(nullable = false)
    private LocalTime matchTime;

    // 구장명
    @Column(nullable = false)
    private String stadiumName;

    // 구장 주소
    private String stadiumAddress;

    // 활동 지역 - 도/시
    private String regionSido;

    // 활동 지역 - 시/군/구
    private String regionSigungu;

    // 모집 성별 (0: 남자, 1: 여자, 2: 무관)
    private Integer genderType;

    // 모집 연령대 (비트마스크: 10대=1, 20대=2, 30대=4, 40대=8, 50대=16, 60대=32, 무관=0)
    private Integer ageGroups;

    // 모집 포지션 (FIELD: 필드, GK: 골키퍼, ANY: 무관)
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private PositionType positionType;

    // 모집 인원
    @Column(nullable = false)
    private Integer maxGuests;

    // 현재 신청 인원
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer currentGuests;

    // 참가비 (0: 무료, 5000, 8000, 10000 등)
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer fee;

    // 보장 시간 (분 단위: 30, 60, 80, 100)
    private Integer guaranteedMinutes;

    // 추가 안내 사항
    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    // 모집 상태 (RECRUITING: 모집중, COMPLETED: 모집완료, CANCELLED: 취소)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RecruitmentStatus status;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamUid", insertable = false, updatable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matchUid", insertable = false, updatable = false)
    private FutsalMatch match;

    // 게스트 신청 관계
    @OneToMany(mappedBy = "recruitment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GuestApplication> applications;

    public enum PositionType {
        FIELD, GK, ANY
    }

    public enum RecruitmentStatus {
        RECRUITING, COMPLETED, CANCELLED
    }
}
