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
 * 캐시 지갑 엔티티
 * 사용자 캐시 잔액 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cash_wallet")
public class CashWallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 사용자 UID
    @Column(nullable = false, unique = true)
    private String userUid;

    // 현재 잔액
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer balance;

    // 누적 충전 금액
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer totalCharged;

    // 누적 사용 금액
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer totalUsed;

    // 누적 환불 금액
    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer totalRefunded;

    // 마지막 충전일시
    private LocalDateTime lastChargedDate;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userUid", insertable = false, updatable = false)
    private User user;
}
