package com.ttwijang.cms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductOrderGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userUid", insertable = false, updatable = false)
    private User user;
    private String userUid;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "shopId", insertable = false, updatable = false)
    private Shop shop;
    private Integer shopId;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "stationId", insertable = false, updatable = false)
    private Station station;
    private Integer stationId;
    private int deliveryFee;
    private String addressDetail;
    private String memo;
    @Enumerated(EnumType.STRING)
    private ProductOrderType orderType;
    private int originAmount;
    private String orderId;
    private int amount;
    private int productNum;
    private Integer usePoint;
    private Integer useCouponIdx;
    private Integer discountAmount;
    private int paymentStatus;
    private int orderStatus;
    private int weekNum;
    private boolean reviewStatus;
    
    private LocalDate startDate;
    private LocalDate endDate;

    @CreationTimestamp
    private LocalDateTime createDate;

    private String paymentKey;
    private String cancelReasonType;
    private String cancelReason;

    // @OneToMany
    // private List<ProductOrder> productOrders;
}
