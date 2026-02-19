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
    @Column(name = "rater_uid", nullable = false)
    private String raterUserUid;

    // 피평가 대상 UID (팀 또는 개인, target_type으로 구분)
    @Column(name = "target_uid", nullable = false)
    private String ratedTeamUid;

    // 관련 매치 UID
    @Column(name = "match_uid", nullable = false)
    private String matchUid;

    // 평가 대상 유형 (USER, TEAM)
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", length = 20)
    private RatingTargetType targetType;

    // 매너 점수 (0.0 ~ 5.0)
    @Column(columnDefinition = "DECIMAL(3,2)")
    private Double score;

    // 평가 코멘트
    @Column(columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rater_uid", insertable = false, updatable = false)
    private User rater;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_uid", insertable = false, updatable = false)
    private Team ratedTeam;

    public enum RatingTargetType {
        USER, TEAM
    }
}
