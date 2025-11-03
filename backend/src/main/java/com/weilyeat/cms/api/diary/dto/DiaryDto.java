package com.ttwijang.cms.api.diary.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class DiaryDto {
    @Data
    public static class physicalConditionDetail { // 영양상태 정보
        private Long idx;
        private Float weight;
        private Float muscleWeight;
        private Float fatRate;
        private LocalDate diaryDate;
        private Float purposeWeight;
        private Float purposeMuscleWeight;
        private Float purposeFatRate;
    }

    @Data
    public static class physicalConditionSave { // 영양상태 저장
        private Float weight;
        private Float muscleWeight;
        private Float fatRate;
        private LocalDate diaryDate;
    }

    @Data
    public static class purposePhysicalSave {
        private Float purposeWeight;
        private Float purposeMuscleWeight;
        private Float purposeFatRate;
        private LocalDate goalDate;
        private LocalDate diaryDate;
    }

    @Data
    public static class detail {
        private DiaryDto.mealDetail breakfast = new DiaryDto.mealDetail();
        private DiaryDto.mealDetail lunch = new DiaryDto.mealDetail();
        private DiaryDto.mealDetail dinner = new DiaryDto.mealDetail();
    }
    
    @Data
    public static class mealDetail {
        private Float totalCalorie = 0f;
        private List<mealItemDetail> mealList = new ArrayList<mealItemDetail>();
    }

    @Data
    public static class mealItemDetail {
        private Long idx;
        private int type;
        private String menuName;
        private Float amount;
        private String amountUnit;
        private Float calorie;
        private Float carbohydrate; // 탄수화물(g)
        private Float protein; // 단백질(g)
        private Float fat; // 지방(g)
        private Float sodium; // 나트륨(mg)
        private Float sugar; // 당(g)
        private Float saturatedFattyAcids; // 포화지방(g)
        private Float vitaminC; // 비타민c(mg)
        private Float transFat; // 트랜스지방(mg)
        private Float cholesterol; // 콜레스테롤(mg)
        private Float calcium; // 칼슘(mg)
        private Float iron; // 철(mg)
        private Float potassium; // 컬륨(mg)
        private Float dietaryFiber; // 식이섬유(g)
    }

    @Data
    public static class mealItemAdd {
        private int type;
        private String menuName;
        private Float amount;
        private String amountUnit;
        private Float calorie;
        private Float carbohydrate; // 탄수화물(g)
        private Float protein; // 단백질(g)
        private Float fat; // 지방(g)
        private Float sodium; // 나트륨(mg)
        private Float sugar; // 당(g)
        private Float saturatedFattyAcids; // 포화지방(g)
        private Float vitaminC; // 비타민c(mg)
        private Float transFat; // 트랜스지방(mg)
        private Float cholesterol; // 콜레스테롤(mg)
        private Float calcium; // 칼슘(mg)
        private Float iron; // 철(mg)
        private Float potassium; // 컬륨(mg)
        private Float dietaryFiber; // 식이섬유(g)
        private String diaryDate;
    }

    @Data
    public static class mealItemAddByFood {
        private Integer foodIdx;
        private Float weight;
        private int type;
        private String diaryDate;
    }

    @Data
    public static class mealItemAddByHistory {
        private Long mealIdx;
        private Float weight;
        private int type;
        private String diaryDate;
    }

    @Data
    public static class mealItemAddByOrder {
        private Integer orderIdx;
        private int type;
        private String diaryDate;
    }

    @Data
    public static class mealStatistics { // 체성분 기록
        private String diaryDate;
        private Float calorie = 0f;
        private Float amount = 0f;
        private Float carbohydrate = 0f; // 탄수화물(g)
        private Float protein = 0f; // 단백질(g)
        private Float fat = 0f; // 지방(g)
        private Float sodium = 0f; // 나트륨(mg)
        private Float sugar = 0f; // 당(g)
        private Float saturatedFattyAcids = 0f; // 포화지방(g)
        private Float vitaminC = 0f; // 비타민c(mg)
        private Float transFat = 0f; // 트랜스지방(mg)
        private Float cholesterol = 0f; // 콜레스테롤(mg)
        private Float calcium = 0f; // 칼슘(mg)
        private Float iron = 0f; // 철(mg)
        private Float potassium = 0f; // 컬륨(mg)
        private Float dietaryFiber = 0f; // 식이섬유(g)

        private Float standardCalorie = 0f;
        private Float standardAmount = 0f;
        private Float standardCarbohydrate = 0f; // 탄수화물(g)
        private Float standardProtein = 0f; // 단백질(g)
        private Float standardFat = 0f; // 지방(g)
        private Float standardSodium = 0f; // 나트륨(mg)
        private Float standardSugar = 0f; // 당(g)
        private Float standardSaturatedFattyAcids = 0f; // 포화지방(g)
        private Float standardVitaminC = 0f; // 비타민c(mg)
        private Float standardTransFat = 0f; // 트랜스지방(mg)
        private Float standardCholesterol = 0f; // 콜레스테롤(mg)
        private Float standardCalcium = 0f; // 칼슘(mg)
        private Float standardIron = 0f; // 철(mg)
        private Float standardPotassium = 0f; // 컬륨(mg)
        private Float standardDietaryFiber = 0f; // 식이섬유(g)
    }
}
