package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 매너 점수 평가 엔티티
 * 경기 후 매너 점수 평가 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "manner_rating")
public class MannerRating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 평가자 사용자 UID
    @Column(nullable = false)
    private String raterUserUid;

    // 피평가자 사용자 UID (개인)
    private String ratedUserUid;

    // 피평가 팀 UID (팀)
    private String ratedTeamUid;

    // 관련 매치 UID
    @Column(nullable = false)
    private String matchUid;

    // 평가 대상 유형 (USER, TEAM)
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private RatingTargetType targetType;

    // 매너 점수 (1.0 ~ 5.0)
    @Column(columnDefinition = "DECIMAL(2,1)")
    private Double score;

    // 평가 코멘트
    @Column(columnDefinition = "TEXT")
    private String comment;

    // 평가 태그 (JSON: ["시간 약속", "페어플레이", "친절함"] 등)
    @Column(columnDefinition = "TEXT")
    private String tags;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raterUserUid", insertable = false, updatable = false)
    private User rater;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ratedUserUid", insertable = false, updatable = false)
    private User ratedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ratedTeamUid", insertable = false, updatable = false)
    private Team ratedTeam;

    public enum RatingTargetType {
        USER, TEAM
    }
}
