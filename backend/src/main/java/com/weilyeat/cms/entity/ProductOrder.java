package com.weilyeat.cms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    @ManyToOne(optional = false)
	@JoinColumn(name = "groupId", insertable = false, updatable = false)
    private ProductOrderGroup group;
    private Integer groupId;
    @ManyToOne(optional = false, fetch = FetchType.LAZY )
	@JoinColumn(name = "productId", insertable = false, updatable = false)
    private Product product;
    private String productName;
    private Long productId;
    @ManyToOne
    @JoinColumn(name = "weekId")
    private ProductOrderWeek week;
    @ManyToOne
    @JoinColumn(name = "dayId")
    private ProductOrderDay day;
    private int weekNum;
    private int dayNum;
    private int amount;
    private int pickupStatus; // 픽업(0: 대기, 1: 픽업완료)
    private int paymentStatus; // 결제완료 여부
    private int orderStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate orderDate;
    private LocalDateTime refundDate;
    private Integer productNum; // 개수
    private Integer settleStatus; // 정산상태(0: 대기, 1: 신청, 2: 정산완료)
    private LocalDateTime settleApplyDate; // 정산상태(0: 대기, 1: 신청, 2: 정산완료)
    private String pickupTime;
    private int receiveStatus; // 입고상태(0: 주문접수, 1: 배달중, 2: 배달완료)
}
