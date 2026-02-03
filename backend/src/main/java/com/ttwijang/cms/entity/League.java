package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDate;
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
 * 리그 엔티티
 * 풋살 리그 정보 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "league")
public class League implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 리그 이름
    @Column(nullable = false, length = 100)
    private String name;

    // 리그 등급 (A, B, C 등)
    @Column(nullable = false, length = 10)
    private String grade;

    // 리그 설명
    @Column(columnDefinition = "TEXT")
    private String description;

    // 시즌 (예: 2025-01)
    @Column(length = 20)
    private String season;

    // 시작일
    private LocalDate startDate;

    // 종료일
    private LocalDate endDate;

    // 활동 지역 - 도/시
    private String regionSido;

    // 활동 지역 - 시/군/구
    private String regionSigungu;

    // 최대 참가 팀 수
    private Integer maxTeams;

    // 현재 참가 팀 수
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer currentTeams;

    // 리그 상태 (RECRUITING: 모집중, IN_PROGRESS: 진행중, COMPLETED: 완료)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private LeagueStatus status;

    // 리그 규칙 (HTML 또는 TEXT)
    @Column(columnDefinition = "TEXT")
    private String rules;

    // 리그 배너 이미지 UID
    private String bannerFileUid;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    // 리그 참가 팀 관계
    @OneToMany(mappedBy = "league", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LeagueTeam> leagueTeams;

    // 리그 경기 관계
    @OneToMany(mappedBy = "league", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LeagueMatch> matches;

    public enum LeagueStatus {
        RECRUITING, IN_PROGRESS, COMPLETED
    }
}
