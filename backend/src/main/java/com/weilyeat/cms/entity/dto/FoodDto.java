package com.weilyeat.cms.entity.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;


public class FoodDto{							

    @Data
    public static class Detail{
        private Long num;
        private String code;
        private String madeBy;
        private String category;
        private String categoryDetail;
        private Float weightOnce;
        private String weightType;
        private Float totalG;
        private Float totalML;
        
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
        // private int inputType;
        private String ref;
        // private LocalDateTime createdDate;
        // private Long accountId;
        private int count;
    }

    @Data
    public static class Add{
        private Float weightOnce;
        private String weightType;
        private Float totalG;
        private Float totalML;
        
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
        private Long accountId;

        // CustomFood Add
        private int type;
        private int qty;
        private Long diaryId;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime date;
    }

}