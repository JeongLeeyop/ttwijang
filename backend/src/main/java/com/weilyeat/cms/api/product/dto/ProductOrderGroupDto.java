package com.weilyeat.cms.api.product.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.weilyeat.cms.api.shop.dto.ShopDto;
import com.weilyeat.cms.api.station.dto.StationDto;

import lombok.Getter;
import lombok.Setter;

public class ProductOrderGroupDto {
    @Getter
    @Setter
    public static class list {
        private int idx;
        private int orderStatus;
        private String orderType;
        private int amount;
        private int deliveryFee;
        private String createDate;
        private LocalDate startDate;
        private LocalDate endDate;
        private int weekNum;
        private String memo;
    }

    @Getter
    @Setter
    public static class detail {
        private int amount;
        private ShopDto.detail shop;
        private StationDto.detail station;
        private String addressDetail;
        private int deliveryFee;
        private int productNum;
        private Integer usePoint;
        private Integer useCouponIdx;
        private Integer discountAmount;
        private int paymentStatus;
        private int orderStatus;
        private String orderType;
        private Map<Integer, weekDetail> week = new HashMap<Integer, weekDetail>();
        private boolean reviewStatus;
        private boolean reviewExpiredStatus;
        private boolean lastPickupStatus;
        private String memo;
    }

    @Getter
    @Setter
    public static class weekDetail {
        private LocalDate startDate;
        private LocalDate endDate;
        private List<product> products = new ArrayList<product>();
    }

    @Getter
    @Setter
    public static class product {
        private Long idx;
        private String productName; // 상품명
        private int amount; // 재고량
        private int price; // 판매가
        private int productNum;
        private int weekNum;
        private int dayNum;
        private String pickupTime;
        private int pickupStatus;
        private int receiveStatus;
        private int orderStatus;
    }

    @Getter
    @Setter
    public static class updateStatus {
        private int orderStatus;
    }
}
