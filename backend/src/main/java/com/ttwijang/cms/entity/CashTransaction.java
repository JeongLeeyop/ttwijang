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
 * 캐시 거래 내역 엔티티
 * 충전, 사용, 환불, 적립, 취소 내역 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cash_transaction")
public class CashTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 지갑 UID
    @Column(nullable = false)
    private String walletUid;

    // 사용자 UID
    @Column(nullable = false)
    private String userUid;

    // 거래 유형 (CHARGE: 충전, USE: 사용, REFUND: 환불, EARN: 적립, CANCEL: 취소)
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType type;

    // 거래 금액 (양수: 입금, 음수: 출금)
    @Column(nullable = false)
    private Integer amount;

    // 거래 후 잔액
    @Column(nullable = false)
    private Integer balanceAfter;

    // 거래 설명
    private String description;

    // 관련 결제 UID
    private String paymentUid;

    // 관련 매치/게스트 UID
    private String referenceUid;

    // 관련 타입 (MATCH, GUEST, SPONSORSHIP 등)
    private String referenceType;

    // 거래 상태 (PENDING: 대기, COMPLETED: 완료, FAILED: 실패, CANCELLED: 취소)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TransactionStatus status;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userUid", insertable = false, updatable = false)
    private User user;

    public enum TransactionType {
        CHARGE, USE, REFUND, EARN, CANCEL
    }

    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED, CANCELLED
    }
}
