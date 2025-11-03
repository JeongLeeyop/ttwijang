package com.ttwijang.cms.api.payment.dto;

import lombok.Getter;
import lombok.Setter;

public class PaymentRequestDto {

    @Getter
    @Setter
    public static class approval {
        private String paymentKey;
        private String orderId;
        private int amount;
    }

    @Getter
    @Setter
    public static class refund {
        private String cancelReasonType;
        private String cancelReason;
        private int cancelAmount;
        private int week;
        private boolean allStatus;
    }
}
