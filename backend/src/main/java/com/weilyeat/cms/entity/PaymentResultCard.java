package com.weilyeat.cms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PaymentResultCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String orderId;
    private String orderName;
    private LocalDate version;
    private String paymentKey;
    private String status;
    private String lastTransactionKey;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private Boolean useEscrow;
    private Boolean cultureExpense;
    private int method;
    private int totalAmount;
    private int balanceAmount;
    private int suppliedAmount;
    private int taxFreeAmount;
    private int vat;
    private String currency;
    private String userUid;
}
