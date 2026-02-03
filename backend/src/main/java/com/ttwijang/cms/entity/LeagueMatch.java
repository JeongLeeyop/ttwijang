package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 리그 경기 엔티티
 * 리그 내 개별 경기 정보 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "league_match")
public class LeagueMatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 리그 UID
    @Column(nullable = false)
    private String leagueUid;

    // 홈팀 UID
    @Column(nullable = false)
    private String homeTeamUid;

    // 원정팀 UID
    @Column(nullable = false)
    private String awayTeamUid;

    // 경기 일자
    @Column(nullable = false)
    private LocalDate matchDate;

    // 경기 시작 시간
    private LocalTime matchTime;

    // 경기 소요 시간 (분)
    private Integer durationMinutes;

    // 구장명
    private String stadiumName;

    // 구장 주소
    private String stadiumAddress;

    // 홈팀 득점
    private Integer homeScore;

    // 원정팀 득점
    private Integer awayScore;

    // 경기 상태 (SCHEDULED: 예정, IN_PROGRESS: 진행중, COMPLETED: 완료, CANCELLED: 취소)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MatchStatus status;

    // 라운드 (1라운드, 2라운드 등)
    private Integer round;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leagueUid", insertable = false, updatable = false)
    private League league;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeTeamUid", insertable = false, updatable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "awayTeamUid", insertable = false, updatable = false)
    private Team awayTeam;

    public enum MatchStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}
