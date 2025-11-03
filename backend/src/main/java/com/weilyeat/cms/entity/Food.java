package com.weilyeat.cms.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Food{							
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

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
    private Float totalg;
    // 총내용량(mL)
    private Float totalml;

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
    // @Comment("입력타입 1.userCustomFood 2.adminCustomFood 3.api")
    private int inputType;

    // @Comment("출처")
    private String ref;

    private Long accountId;

    private boolean deleteStatus;

    @CreationTimestamp
    private LocalDateTime createDate;
        
    @ManyToOne
    @JoinColumn(name="accountId",updatable = false,insertable = false)
    private Account account;
}