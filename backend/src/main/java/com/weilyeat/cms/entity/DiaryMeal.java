package com.weilyeat.cms.entity;

import java.time.LocalDate;
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
public class DiaryMeal {							
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String userUid;

    private Integer foodIdx;

    private int type;

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

    @Column(name = "vitamin_c")
    private Float vitaminC; // 비타민c(mg)
    
    private Float transFat; // 트랜스지방(mg)

    private Float cholesterol; // 콜레스테롤(mg)

    private Float calcium; // 칼슘(mg)

    private Float iron; // 철(mg)

    private Float potassium; // 컬륨(mg)

    private Float dietaryFiber; // 식이섬유(g)

    private LocalDate diaryDate; // 다이어리 날짜

    @CreationTimestamp
    private LocalDateTime createDate; // 등록일자
}