package com.weilyeat.cms.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class ProductPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    
    private String name; // 패키지 상품명
    private String description; // 설명
    private String thumbUid; // 썸네일 파일 고유값
    private int price; // 판매가
    private int deliveryFee; // 배송비
    private boolean sellStatus; // 판매상태
    
    private Integer maxWeekCount; // 최대 배송 주차 수
    private Integer deliveryDaysPerWeek; // 주당 배송 일수
    private Integer selectableProductCount; // 선택 가능한 상품 갯수
    
    @CreationTimestamp
    private LocalDateTime createDate; // 생성일
    private String createUserUid; // 생성 유저 고유값
    private LocalDateTime modifyDate; // 수정일
    private String modifyUserUid; // 수정 유저 고유값
    private boolean deleteStatus;
    
    @Enumerated(EnumType.STRING)
    private ProductType productType; // STATION
    
    // 패키지에 포함된 상품 목록
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "packageIdx")
    private List<ProductPackageItem> packageItems = new ArrayList<>();
}
