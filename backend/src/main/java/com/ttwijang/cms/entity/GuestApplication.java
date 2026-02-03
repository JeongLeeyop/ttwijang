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
 * 게스트 신청 엔티티
 * 용병 신청 정보 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "guest_application", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"recruitmentUid", "userUid"})
})
public class GuestApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 모집 UID
    @Column(nullable = false)
    private String recruitmentUid;

    // 신청자 사용자 UID
    @Column(nullable = false)
    private String userUid;

    // 신청 상태 (PENDING: 대기, APPROVED: 승인, REJECTED: 거절, CANCELLED: 취소)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ApplicationStatus status;

    // 신청자 포지션
    private String position;

    // 신청자 나이
    private Integer age;

    // 신청 메시지
    @Column(columnDefinition = "TEXT")
    private String message;

    // 결제 완료 여부
    private Boolean paymentCompleted;

    // 결제 UID
    private String paymentUid;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private LocalDateTime processedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitmentUid", insertable = false, updatable = false)
    private GuestRecruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userUid", insertable = false, updatable = false)
    private User user;

    public enum ApplicationStatus {
        PENDING, APPROVED, REJECTED, CANCELLED
    }
}
