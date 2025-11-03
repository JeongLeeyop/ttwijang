package com.ttwijang.cms.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private String name;

    private String type;

    private boolean percentStatus;

    private Integer discountPercent;

    private Integer discountPrice;

    private Integer giveStandardPrice;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime expiredDate;

    @CreationTimestamp
    private LocalDateTime createDate;

    private boolean deleteStatus;
}
