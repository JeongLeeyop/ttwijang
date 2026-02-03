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
 * 매치 신청 엔티티
 * 팀 대 팀 매치 신청 정보 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "match_application")
public class MatchApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 매치 UID
    @Column(nullable = false)
    private String matchUid;

    // 신청 팀 UID
    @Column(nullable = false)
    private String applicantTeamUid;

    // 신청자 사용자 UID
    @Column(nullable = false)
    private String applicantUserUid;

    // 신청 상태 (PENDING: 대기, APPROVED: 승인, REJECTED: 거절, CANCELLED: 취소)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ApplicationStatus status;

    // 신청 메시지
    @Column(columnDefinition = "TEXT")
    private String message;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private LocalDateTime processedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matchUid", insertable = false, updatable = false)
    private FutsalMatch match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicantTeamUid", insertable = false, updatable = false)
    private Team applicantTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicantUserUid", insertable = false, updatable = false)
    private User applicantUser;

    public enum ApplicationStatus {
        PENDING, APPROVED, REJECTED, CANCELLED
    }
}
