package com.ttwijang.cms.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "settlement_config")
public class SettlementConfig {

    @Id
    private Long id; // 항상 1 (단일 행)

    // 경기 종료 후 정산 가능까지의 대기 일수
    @Column(nullable = false)
    private Integer settlementDays;

    // Toss 월별 자동이체 사용 여부 — ON이면 매월 autoSettleDay일 오전 2시에 Toss API로 자동 이체
    @Column(nullable = false)
    private Boolean useTossPayout;

    // 자동 정산 실행 일 (매월 N일, 기본 1일)
    @Column(nullable = false)
    private Integer autoSettleDay;

    // 정산 비고/안내문
    @Column(length = 500)
    private String description;
}
