package com.ttwijang.cms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PaymentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    private String orderId;
    private String orderName;
    private int status;
    private String userUid;

    public PaymentRequest(String orderId, String orderName, String userUid) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.userUid = userUid;
    }
}
