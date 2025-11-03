package com.ttwijang.cms.api.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

import com.ttwijang.cms.api.payment.constant.PaymentMethod;

public class PaymentDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class createKey {
        private String key;
        private int amount;
    }

    @Getter
    @Setter
    public static class tryRefund {
        private String orderId;
    }

    @Getter
    @Setter
    public static class refund {
        private String cancelReason;
        private int cancelAmount;
    }

}
