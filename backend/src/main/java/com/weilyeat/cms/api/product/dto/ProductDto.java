package com.weilyeat.cms.api.product.dto;

import com.weilyeat.cms.entity.ProductType;

import lombok.Getter;
import lombok.Setter;

public class ProductDto {
    @Getter
    @Setter
    public static class list {
        private Long idx;
        private String name; // 상품명
        private String description; // 설명
        private String thumbUid; // 썸네일 파일 고유값
        private float calorie; // 칼로리
        private int weight; // 무게
        private int amount; // 재고량
        private int price; // 판매가
        private int deliveryFee; // 배송비
        private String dietType; // 식단형태
        private String dietPurpose; // 식단관리 목적
        private String dietPrecaution; // 식단 제공 시 유의사항
        private boolean sellStatus; // 판매상태
        
        private ProductType productType;
        private boolean extraProductStatus;
        private Integer weekDeliveryCnt;
    }

    @Getter
    @Setter
    public static class detail {
        private Long idx;
        private String name; // 상품명
        private String description; // 설명
        private String material; // 재료
        private String thumbUid; // 썸네일 파일 고유값
        private float calorie; // 칼로리
        private int weight; // 무게
        private int amount; // 재고량
        private int price; // 판매가
        private int deliveryFee; // 배송비
        private float carbohydrate; // 탄수화물
        private float protein; // 단백질
        private float fat; // 지방
        private float sodium; // 나트륨
        private float sugar; // 당
        private float saturatedfat; // 포화지방산
        private float transFat; // 트랜스지방
        private float cholesterol; // 콜레스테롤
        private float calcium; // 칼슘
        private float iron; // 철
        private float potassium; // 칼륨
        private float dietaryFiber; // 식이섬유
        private float vitaminc; //비타민C
        private String dietType; // 식단형태
        private String dietPurpose; // 식단관리 목적
        private String dietPrecaution; // 식단 제공 시 유의사항
        private boolean sellStatus; // 판매상태
        
        private ProductType productType;
        private boolean extraProductStatus;
        private Integer weekDeliveryCnt;
    }
}
