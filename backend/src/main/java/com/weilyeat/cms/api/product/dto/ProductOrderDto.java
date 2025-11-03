package com.ttwijang.cms.api.product.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.ttwijang.cms.entity.ProductOrderType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ProductOrderDto {

    @Getter
    @Setter
    @ToString
    public static class order {
        private int count;
        private int amount;
        private int deliveryFee;
        private Integer shopId;
        private Integer stationId;
        private String addressDetail;
        private String memo;
        private String orderType;
        private Map<Integer, orderChildWeek> week;
        private Integer usePoint;
        private Integer useCouponIdx;
        private String pickupTime;
    }

    @Getter
    @Setter
    @ToString
    public static class orderChildWeek {
        private String startDate;
        private String endDate;
        private Map<Integer, orderChildDay> day;
    }
    @Getter
    @Setter
    @ToString
    public static class orderChildDay {
        private int count;
        private Map<Long, orderChildProducts> products;
    }

    @Getter
    @Setter
    @ToString
    public static class orderChildProducts {
        private int count;
        private orderChildProduct product;
    }

    @Getter
    @Setter
    @ToString
    public static class orderChildProduct {
        private Long idx;
        private String name; // 상품명
        private String description; // 설명
        private String thumbUid; // 썸네일 파일 고유값
        private int calorie; // 칼로리
        private int weight; // 무게
        private int amount; // 재고량
        private int price; // 판매가
        private String dietType; // 식단형태
        private String dietPurpose; // 식단관리 목적
        private String dietPrecaution; // 식단 제공 시 유의사항
        private boolean sellStatus; // 판매상태
        private int count;
    }

    @Data
    public static class lastOrder {
        private LocalDate pickupDate;
        private boolean pickupStatus;
    }

    @Data
    public static class orderStatus {
        private int totalCnt;
        private int pickupCnt;
        private int receiveCnt;
        private int deliveryReadyCnt;
        private int deliveryStartCnt;
        private int deliveryCompleteCnt;
    }

    @Data
    public static class list {
        private Integer idx;
        private String productName;
        private int calorie;
        private int carbohydrate; // 탄수화물
        private int protein; // 단백질
        private int fat; //지방
        private int sodium; //나트륨
        private int sugar; //당
        private int saturatedfat; //포화지방산
    }

    @Data
    public static class orderCheck {
        private String startDate;
        private String endDate;
        private List<Integer> dayNumList;
    }

    @Data
    public static class orderCountList {
        private Map<Integer, orderCountChildWeek> week;
        private Integer shopIdx;
    }
    
    @Data
    public static class orderCountChildWeek {
        private String startDate;
        private String endDate;
        private Map<Integer, orderCountChildDay> day;
    }
    @Data
    public static class orderCountChildDay {
        private int count;
        private int orderCount;
    }

    @Data 
    public static class dayCount {
        private int dayNum;
        private int count;
    }

    @Data
    public static class groupOrderCount {
        private String startDate;
        private String endDate;
        private int weekNum;
        private int dayNum;
        private int count;
    }
}
