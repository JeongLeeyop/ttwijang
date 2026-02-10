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
 * 매치 엔티티
 * 친선경기, 자체경기 등 일반 매치 정보 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "futsal_match")
public class FutsalMatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 주최 팀 UID
    @Column(nullable = false)
    private String hostTeamUid;

    // 상대 팀 UID (매칭 완료 시)
    private String guestTeamUid;

    // 매치 타입 (FRIENDLY: 친선경기, FREE: 자체경기)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MatchType matchType;

    // 매치 방식 (4vs4, 5vs5, 6vs6, 7vs7)
    @Column(length = 10)
    private String matchFormat;

    // 경기 일자
    @Column(nullable = false)
    private LocalDate matchDate;

    // 경기 시작 시간
    @Column(nullable = false)
    private LocalTime matchTime;

    // 경기 소요 시간 (1시간, 2시간, 3시간, 4시간)
    private Integer durationHours;

    // 구장명
    @Column(nullable = false)
    private String stadiumName;

    // 구장 주소
    private String stadiumAddress;

    // 참가비 (팀당)
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer fee;

    // 활동 지역 - 도/시
    private String regionSido;

    // 활동 지역 - 시/군/구
    private String regionSigungu;

    // 매치 상태 (RECRUITING: 모집중, MATCHED: 매칭완료, IN_PROGRESS: 진행중, COMPLETED: 완료, CANCELLED: 취소)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FutsalMatchStatus status;

    // 홈팀 득점
    private Integer homeScore;

    // 원정팀 득점
    private Integer awayScore;

    // 추가 안내 사항
    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    // 모집 마감일
    private LocalDateTime recruitmentDeadline;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostTeamUid", insertable = false, updatable = false)
    private Team hostTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestTeamUid", insertable = false, updatable = false)
    private Team guestTeam;

    // 매치 신청 관계
    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MatchApplication> applications;

    public enum MatchType {
        FRIENDLY, FREE
    }

    public enum FutsalMatchStatus {
        RECRUITING, MATCHED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}
