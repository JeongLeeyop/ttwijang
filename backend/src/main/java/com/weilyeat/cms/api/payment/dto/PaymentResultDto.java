package com.weilyeat.cms.api.payment.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

public class PaymentResultDto {

    @Getter
    @Setter
    public static class success {
        private String orderId;
        private String orderName;
        private LocalDate version;
        private String paymentKey;
        private String status;
        private String lastTransactionKey;
        private String requestedAt;
        private String approvedAt;
        private Boolean useEscrow;
        private Boolean cultureExpense;
        private String method;
        private int totalAmount;
        private int balanceAmount;
        private int suppliedAmount;
        private int taxFreeAmount;
        private int vat;
        private String currency;
        private String userUid;
    }
}
