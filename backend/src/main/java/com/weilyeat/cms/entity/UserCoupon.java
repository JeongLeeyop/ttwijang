package com.weilyeat.cms.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private Integer couponIdx;

    private String userUid;
    
    private Integer orderIdx;

    private String name;

    private String type;

    private boolean percentStatus;

    private Integer discountPercent;

    private Integer discountPrice;

    private LocalDateTime expiredDate;

    @CreationTimestamp
    private LocalDateTime createDate;

    private boolean downloadStatus;

    private LocalDateTime downloadDate;

    private boolean useStatus;

    private LocalDateTime useDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "orderIdx", insertable = false, updatable = false)
    private ProductOrderGroup orderGroup;
}
