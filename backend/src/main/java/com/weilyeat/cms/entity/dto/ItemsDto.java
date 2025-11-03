package com.ttwijang.cms.entity.dto;

import com.ttwijang.cms.entity.Food;

import lombok.Data;

public class ItemsDto{

    @Data
    public static class Detail{
        
        private Long id;
        private String name;
        private String material;
        private String description;
        private String weight;
        private String supplier;
        private String calorie;
        private String price;
        private String status;
        private String dietType;
        private String dietPurpose;
        private String dietPrecaution;
        private String itemFileUid;
        private Long shopId;
    }

    
    @Data
    public static class Diary{
        private Long id;
        private String name;
        private String material;
        private String description;
        private String weight;
        private String supplier;
        private String calorie;
        private String price;
        private String status;
        private String dietType;
        private String dietPurpose;
        private String dietPrecaution;
        private String itemFileUid;
        private Long shopId;
        private FoodDto.Detail food;
    }


    @Data
    public static class Add{
        private String name;
        private String material;
        private String description;
        private String weight;
        private String supplier;
        private String calorie;
        private String price;
        private String dietType;
        private String dietPurpose;
        private String dietPrecaution;
        private String itemFileUid;
        private Long shopId;
    }
}