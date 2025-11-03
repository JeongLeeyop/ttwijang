package com.ttwijang.cms.api.product.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class AdmProductOrderDto {

    @Getter
    @Setter
    public static class list {
        private Integer idx;
        private int amount;
        private int productNum;
        private String pickupTime;
        private int weekNum;
        private int pickupStatus;
        private LocalDateTime orderDate;
        private LocalDateTime refundDate;
        private String productName;
        private groupInfo group;
        private LocalDate pickupDate;
    }

    @Getter
    @Setter
    public static class listByDay {
        private int groupIdx;
        private int idx;
        private String shopName;
        private String stationName;
        private String address;
        private String addressDetail;
        private String name;
        private String number;
        private int pickupStatus;
        private String createDate;
        private String pickupDate;
        private String pickupTime;
        private int dayId;
        private int amount;
        private int productNum;
        private String startDate;
        private String endDate;
        private String memo;
        private int weekNum;
        private int dayNum;

        // 엑셀 전용
        private String productName;
        private int productPrice;
        private int productNum2;
    }

    @Getter
    @Setter
    public static class listByProduct {
        private Integer productId;
        private String productName;
        private int totalProductNum;
        private int totalAmount;
    }

    @Getter
    @Setter
    public static class listByShop {
        private Integer shopId;
        private String shopName;
        private Integer stationId;
        private String stationName;
        private String address;
        private String addressDetail;
        private String shopTel;
        private int totalProductNum;
        private int totalAmount;
    }

    @Getter
    @Setter
    public static class listForFCM {
        private int groupId;
        private String userUid;
        private int weekNum;
        private int dayNum;
        private String name;
        private String pickupTime;
    }

    @Getter
    @Setter
    public static class groupInfo {
        private userInfo user;
        private shopInfo shop;
        private stationInfo station;
        private String memo;
    }

    @Getter
    @Setter
    public static class userInfo {
        private String actualName;
        private String concatNumber;
    }

    @Getter
    @Setter
    public static class shopInfo {
        private String name;
    }
    
    @Getter
    @Setter
    public static class stationInfo {
        private String name;
    }

    @Getter
    @Setter
    public static class detail {
        private Integer idx;
        private int amount;
        private int productNum;
        private String pickupTime;
        private int weekNum;
        private int pickupStatus;
        private String productName;
        private groupInfo group;
        private LocalDate pickupDate;
    }

    @Getter
    @Setter
    public static class detailByDay {
        private Integer idx;
        private int amount;
        private int productNum;
        private String pickupTime;
        private int weekNum;
        private int pickupStatus;
        private String productName;
        private groupInfo group;
        private LocalDate pickupDate;
    }

    @Getter
    @Setter
    public static class detailByProduct {
        private Integer shopIdx;
        private String shopName;
        private Integer stationIdx;
        private String stationName;
        private int totalProductNum;
        private int totalAmount;
    }

    @Getter
    @Setter
    public static class detailByShop {
        private Integer productId;
        private String productName;
        private int totalProductNum;
        private int totalAmount;
        private int totalHoldCount; // 주문접수 수
        private int totalDeliveryCount; // 배달중 수
        private int totalDoneCount; // 배달완료 수
    }

    @Getter
    @Setter
    public static class updateStatus {
        private int pickupStatus;
    }

    @Getter
    @Setter
    public static class updateReceiveStatus {
        private int receiveStatus;
        private List<Integer> idxList;
    }

    @Getter
    @Setter
    public static class totalStatistics {
        private int totalProductNum;
        private int totalAmount;
        private int totalShop;
    }
}
