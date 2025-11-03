package com.weilyeat.cms.api.diary.dto;

import java.time.LocalDate;

import lombok.Data;

public class DiaryMealDto {
    @Data
    public static class list {
        private Long idx;
        private Integer foodIdx;
        private String menuName;
        private Float amount;
        private String amountUnit;
        private Float calorie; // 칼로리
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
        private LocalDate diaryDate; // 다이어리 날짜
    }
}
