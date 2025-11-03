package com.weilyeat.cms.api.settle.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class AdmSettleApplyDto {
    @Data
    public static class list {
        private Integer idx;
        private String shopName;
        private Integer totalSale;
        private Integer totalSettle;
        private Integer totalProductNum;
        private LocalDateTime applyDate;
        private LocalDateTime approvalDate;
        private boolean approvalStatus;
        private int totalOrder;
    }

    @Data
    public static class detail {
        private Integer idx;
        private String shopName;
        private Integer totalSale;
        private Integer totalSettle;
        private Integer totalProductNum;
        private String applyDate;
        private boolean approvalStatus;
        private String approvalDate;
        private List<AdmSettleApplyDto.item> items = new ArrayList<AdmSettleApplyDto.item>();
    }

    @Data
    public static class item {
        private String productName;
        private Integer productNum;
        private Integer amount;
        private Integer weekNum;
        private Integer dayNum;
        private String orderDate;
        private String pickupDate;
    }

    @Data
    public static class approval {
        private List<Integer> applyIdxList = new ArrayList<Integer>();
    }
}
