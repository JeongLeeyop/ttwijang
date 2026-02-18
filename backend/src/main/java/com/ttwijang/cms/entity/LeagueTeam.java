package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 리그 참가 팀 엔티티
 * 리그에 참가한 팀의 전적 및 순위 정보 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "league_team", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"leagueUid", "teamUid"})
})
public class LeagueTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 리그 UID
    @Column(nullable = false)
    private String leagueUid;

    // 팀 UID
    @Column(nullable = false)
    private String teamUid;

    // 순위
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer ranking;

    // 경기 수
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer played;

    // 승
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer wins;

    // 무
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer draws;

    // 패
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer losses;

    // 득점
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer goalsFor;

    // 실점
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer goalsAgainst;

    // 득실차
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer goalDifference;

    // 승점
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer points;

    // 매너 점수 (리그 내)
    @Column(columnDefinition = "DECIMAL(3,2) DEFAULT 0.0")
    private Double mannerScore;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leagueUid", insertable = false, updatable = false)
    private League league;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamUid", insertable = false, updatable = false)
    private Team team;
}
