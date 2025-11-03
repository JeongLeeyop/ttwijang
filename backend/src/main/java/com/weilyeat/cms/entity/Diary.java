package com.weilyeat.cms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String userUid;

    private Float weight;

    private Float muscleWeight;

    private Float fatRate;

    private LocalDate diaryDate;

    private Float purposeWeight;

    private Float purposeMuscleWeight;

    private Float purposeFatRate;

    @CreationTimestamp
    private LocalDateTime createDate;
}