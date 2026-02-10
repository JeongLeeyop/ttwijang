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
 * 팀 후원 엔티티
 * 구단주 및 1회 후원 정보 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "team_sponsorship")
public class TeamSponsorship implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 팀 UID
    @Column(nullable = false)
    private String teamUid;

    // 후원자 UID (sponsorUserUid의 별칭)
    @Column(nullable = false)
    private String sponsorUid;

    // 후원자 사용자 UID
    @Column(nullable = false)
    private String sponsorUserUid;

    // 후원 유형 (OWNER: 구단주, ONE_TIME: 1회 후원, REGULAR: 정기 후원)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SponsorshipType type;

    // 총 후원 금액 (정기 후원의 경우 누적 금액)
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer totalAmount;

    // 후원 금액
    @Column(nullable = false)
    private Integer amount;

    // 후원 상태 (PENDING: 대기, ACTIVE: 활성, EXPIRED: 만료, CANCELLED: 취소)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SponsorshipStatus status;

    // 후원 메시지
    @Column(columnDefinition = "TEXT")
    private String message;

    // 정기 후원 주기 (월 단위)
    private Integer recurringMonths;

    // 다음 정기 후원일
    private LocalDateTime nextPaymentDate;

    // 후원 시작일
    private LocalDateTime startDate;

    // 후원 종료일
    private LocalDateTime endDate;

    // 관련 결제 UID
    private String paymentUid;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamUid", insertable = false, updatable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsorUserUid", insertable = false, updatable = false)
    private User sponsor;

    public enum SponsorshipType {
        OWNER, ONE_TIME, REGULAR
    }

    public enum SponsorshipStatus {
        PENDING, ACTIVE, EXPIRED, CANCELLED
    }
}
