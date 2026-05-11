package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 리그 경기 점수 제출 엔티티
 * 양측 팀 관리자(OWNER/MANAGER)가 각자 점수를 제출 — 일치 시 자동 확정
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "league_match_score_submission",
    uniqueConstraints = @UniqueConstraint(columnNames = {"matchUid", "teamUid"})
)
public class LeagueMatchScoreSubmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    @Column(nullable = false)
    private String matchUid;

    @Column(nullable = false)
    private String teamUid;

    @Column(nullable = false)
    private String submittedByUid;

    @Column(nullable = false)
    private Integer homeScore;

    @Column(nullable = false)
    private Integer awayScore;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
