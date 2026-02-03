package com.ttwijang.cms.api.product.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ProductPackageDto {
    
    @Getter @Setter
    public static class list {
        private Long idx;
        private String name;
        private String thumbUid;
        private int price;
        private int deliveryFee;
        private boolean sellStatus;
        private Integer maxWeekCount;
        private Integer deliveryDaysPerWeek;
        private Integer selectableProductCount;
        private LocalDateTime createDate;
    }
    
    @Getter @Setter
    public static class detail {
        private Long idx;
        private String name;
        private String description;
        private String thumbUid;
        private int price;
        private int deliveryFee;
        private boolean sellStatus;
        private Integer maxWeekCount;
        private Integer deliveryDaysPerWeek;
        private Integer selectableProductCount;
        private LocalDateTime createDate;
        private List<PackageItem> packageItems;
    }
    
    @Getter @Setter
    public static class PackageItem {
        private Long idx;
        private Long productIdx;
        private String productName;
        private String productThumbUid;
        private int productPrice;
    }
    
    @Getter @Setter
    public static class add {
        private String name;
        private String description;
        private String thumbUid;
        private int price;
        private int deliveryFee;
        private boolean sellStatus;
        private Integer maxWeekCount;
        private Integer deliveryDaysPerWeek;
        private Integer selectableProductCount;
        private String productType;
        private List<Long> productIdxList; // 선택된 상품 ID 리스트
    }
    
    @Getter @Setter
    public static class update {
        private String name;
        private String description;
        private String thumbUid;
        private int price;
        private int deliveryFee;
        private boolean sellStatus;
        private Integer maxWeekCount;
        private Integer deliveryDaysPerWeek;
        private Integer selectableProductCount;
        private List<Long> productIdxList; // 선택된 상품 ID 리스트
    }
}
