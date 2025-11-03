package com.weilyeat.cms.api.food.dto;

import lombok.Data;

public class FoodDto {
    @Data
    public static class list {
        private Long idx;
        // 식품코드
        private String code;
        // 지역 / 제조사
        private String madeBy;
        // 식품대분류
        private String category;
        // 식품상세분류
        private String categoryDetail;
        // 1회제공량
        private Float weightOnce;
        // 내용량_단위
        private String weightType;
        // 총내용량(g)
        private Float totalG;
        // 총내용량(mL)
        private Float totalML;
        // @Comment("이름")	
        private String name;
        // @Comment("칼로리 kcal")
        private Float kcal;
        // @Comment("탄수화물 g")
        private Float carbohydrate;
        // @Comment("단백질 g")
        private Float protein;
        // @Comment("지방 g")
        private Float fat;
        // @Comment("나트륨 mg")
        private Float natrium;
        // @Comment("당 g")
        private Float sugar;
        // @Comment("포화지방산 g")
        private Float stFattyAcid;
        // @Comment("트랜스지방 g")
        private Float transFat;
        // @Comment("콜레스테롤 mg")
        private Float cholesterol;
        // @Comment("칼슘 mg")
        private Float calcium;
        // @Comment("철 mg")
        private Float iron;
        // @Comment("철 ug")
        private Float iron2;
        // @Comment("칼륨 mg")
        private Float potassium;
        // @Comment("식이섬유 g")
        private Float dietaryFiber;
        // @Comment("비타민C mg")
        private Float vitaminC;
        // 수분(g)
        private Float moisture;
        // 카페인(㎎)
        private Float caffeine;
        // @Comment("출처")
        private String ref;
    }
}
