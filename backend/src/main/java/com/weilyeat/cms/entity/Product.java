package com.ttwijang.cms.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String name; // 상품명
    private String description; // 설명
    private String thumbUid; // 썸네일 파일 고유값
    private int weight; // 무게
    private String material; // 재료
    private int amount; // 재고량
    private int price; // 판매가
    private String dietType; // 식단형태
    private String dietPurpose; // 식단관리 목적
    private String dietPrecaution; // 식단 제공 시 유의사항
    private boolean sellStatus; // 판매상태
    @CreationTimestamp
    private LocalDateTime createDate; // 생성일
    private String createUserUid; // 생성 유저 고유값
    private LocalDateTime modifyDate; // 수정일
    private String modifyUserUid; // 수정 유저 고유값
    private boolean allStatus;
    private float calorie; // 칼로리
    private float carbohydrate; // 탄수화물
    private float protein; // 단백질
    private float fat; //지방
    private float sodium; //나트륨
    private float sugar; //당
    private float saturatedfat; //포화지방산
    private float transFat; //포화지방산
    private float cholesterol; //포화지방산
    private float calcium; //포화지방산
    private float iron; //포화지방산
    private float potassium; //포화지방산
    private float dietaryFiber; //포화지방산
    private float vitaminc; //포화지방산
    private boolean deleteStatus;
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    private boolean extraProductStatus;
    private Integer weekDeliveryCnt;
    private int deliveryFee;
}
