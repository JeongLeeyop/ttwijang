package com.ttwijang.cms.api.food.dto;

import lombok.Data;

public class AdmFoodDto {
    @Data
    public static class list {
        private Integer idx;
        private String code;
        private String madeBy;
        private String category;
        private String categoryDetail;
        private Float weightOnce;
        private String weightType;
        private Float totalg;
        private Float totalml;
        private String name;
        private Float kcal;
        private Float carbohydrate;
        private Float protein;
        private Float fat;
        private int inputType;
        private String ref;
    }

    @Data
    public static class detail {
        private Integer idx;
        private String code;
        private String madeBy;
        private String category;
        private String categoryDetail;
        private Float weightOnce;
        private String weightType;
        private Float totalg;
        private Float totalml;
        private String name;
        private Float kcal;
        private Float carbohydrate;
        private Float protein;
        private Float fat;
        private Float natrium;
        private Float sugar;
        private Float stFattyAcid;
        private Float transFat;
        private Float cholesterol;
        private Float calcium;
        private Float iron;
        private Float iron2;
        private Float potassium;
        private Float dietaryFiber;
        private Float vitaminC;
        private Float moisture;
        private Float caffeine;
        private int inputType;
        private String ref;
    }

    @Data
    public static class add {
        private String code;
        private Float weightOnce;
        private String weightType;
        private Float totalg;
        private Float totalml;
        private String name;
        private Float kcal;
        private Float carbohydrate;
        private Float protein;
        private Float fat;
        private Float natrium;
        private Float sugar;
        private Float stFattyAcid;
        private Float transFat;
        private Float cholesterol;
        private Float calcium;
        private Float iron;
        private Float iron2;
        private Float potassium;
        private Float dietaryFiber;
        private Float vitaminC;
        private Float moisture;
        private Float caffeine;
    }

    @Data
    public static class update {
        private String code;
        private String madeBy;
        private String category;
        private String categoryDetail;
        private Float weightOnce;
        private String weightType;
        private Float totalg;
        private Float totalml;
        private String name;
        private Float kcal;
        private Float carbohydrate;
        private Float protein;
        private Float fat;
        private Float natrium;
        private Float sugar;
        private Float stFattyAcid;
        private Float transFat;
        private Float cholesterol;
        private Float calcium;
        private Float iron;
        private Float iron2;
        private Float potassium;
        private Float dietaryFiber;
        private Float vitaminC;
        private Float moisture;
        private Float caffeine;
    }
}
